package com.vjshow.marketplace.OAuth2;

import java.io.IOException;

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
	public void onAuthenticationSuccess(

			HttpServletRequest request, HttpServletResponse response, Authentication authentication

	) throws IOException {

		OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
		
		String uri = request.getRequestURI();

		AuthProviderEnum provider = AuthProviderEnum.GOOGLE;

		if (uri.contains("facebook")) provider = AuthProviderEnum.FACEBOOK;
		if (uri.contains("zalo")) provider = AuthProviderEnum.ZALO;

		String providerId = oauthUser.getAttribute("sub") != null ? oauthUser.getAttribute("sub") : oauthUser.getAttribute("id");
		
		String email = oauthUser.getAttribute("email");

		if(email == null){
		    email = providerId + "@facebook.com";
		}
		
		String name = oauthUser.getAttribute("name");
		
		String picture;

		if(provider.equals(AuthProviderEnum.FACEBOOK)){
		    picture = "https://graph.facebook.com/" + providerId + "/picture?type=large";
		}else{
		    picture = oauthUser.getAttribute("picture");
		}


		AuthResponse authResponse = authFacade.loginOAuth(

				provider,

				providerId,

				email,

				name,

				picture

		);
		
		
		String redirectUrl = "http://localhost:3000/?token=" 
		        + authResponse.getAccessToken();

		response.sendRedirect(redirectUrl);

	}

}