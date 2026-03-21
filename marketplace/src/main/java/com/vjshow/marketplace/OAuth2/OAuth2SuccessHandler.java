package com.vjshow.marketplace.OAuth2;

import java.io.IOException;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.vjshow.marketplace.dto.response.AuthResponse;
import com.vjshow.marketplace.enums.AuthProviderEnum;
import com.vjshow.marketplace.facade.AuthFacade;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final AuthFacade authFacade;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {

		OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

		String uri = request.getRequestURI();
		AuthProviderEnum provider = AuthProviderEnum.GOOGLE;
		if (uri.contains("facebook"))
			provider = AuthProviderEnum.FACEBOOK;
		if (uri.contains("zalo"))
			provider = AuthProviderEnum.ZALO;

		Map<String, Object> attributes = oauthUser.getAttributes();

		String providerId = getProviderId(provider, attributes);
		String email = getEmail(provider, attributes, providerId);
		String name = (String) attributes.getOrDefault("name", "Unknown");
		String picture = getPicture(provider, attributes, providerId);

		AuthResponse authResponse = authFacade.loginOAuth(provider, providerId, email, name, picture);

		String redirectUrl = "http://localhost:3000/?token=" + authResponse.getAccessToken();

		response.sendRedirect(redirectUrl);
	}

	private String getProviderId(AuthProviderEnum provider, Map<String, Object> attr) {
		return switch (provider) {
		case GOOGLE -> (String) attr.get("sub");
		case FACEBOOK -> (String) attr.get("id");
		case ZALO -> (String) attr.get("id"); // chỉnh theo API Zalo
		default -> throw new RuntimeException("Unsupported provider");
		};
	}

	private String getEmail(AuthProviderEnum provider, Map<String, Object> attr, String providerId) {
		String email = (String) attr.get("email");

		if (email == null) {
			return providerId + "@" + provider.name().toLowerCase() + ".com";
		}

		return email;
	}

	private String getPicture(AuthProviderEnum provider, Map<String, Object> attr, String providerId) {
		return switch (provider) {
		case FACEBOOK -> "https://graph.facebook.com/" + providerId + "/picture?type=large";
		case GOOGLE -> (String) attr.get("picture");
		case ZALO -> (String) attr.get("picture"); // tùy API zalo
		default -> throw new RuntimeException("Unsupported provider");
		};
	}
}
