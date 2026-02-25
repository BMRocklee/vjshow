package com.vjshow.marketplace.OAuth2;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.vjshow.marketplace.dto.response.AuthResponse;
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

		OAuth2User user = (OAuth2User) authentication.getPrincipal();

		String provider = request.getRequestURI().contains("facebook") ? "FACEBOOK" : "GOOGLE";

		String providerId = user.getAttribute("sub") != null ? user.getAttribute("sub") : user.getAttribute("id");

		AuthResponse authResponse = authFacade.loginOAuth(

				provider,

				providerId,

				user.getAttribute("email"),

				user.getAttribute("name"),

				user.getAttribute("picture")

		);

		response.setContentType("application/json");

		response.getWriter().write(

				"""
						{
						 "accessToken": "%s",
						 "tokenType": "%s",
						 "expiresIn": %d
						}
						""".formatted(

						authResponse.getAccessToken(),

						authResponse.getTokenType(),

						authResponse.getExpiresIn()

				)

		);

	}

}