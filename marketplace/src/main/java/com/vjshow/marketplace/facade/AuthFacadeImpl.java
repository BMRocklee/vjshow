package com.vjshow.marketplace.facade;

import org.springframework.stereotype.Component;

import com.vjshow.marketplace.dto.response.AuthResponse;
import com.vjshow.marketplace.entity.UserEntity;
import com.vjshow.marketplace.enums.AuthProviderEnum;
import com.vjshow.marketplace.service.JwtService;
import com.vjshow.marketplace.service.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthFacadeImpl implements AuthFacade {
	
    private final JwtService jwtService;
	
	private final UserService userService;

	@Override
	public AuthResponse loginOAuth(AuthProviderEnum provider, String providerId, String email, String name, String picture) {
		// TODO Auto-generated method stub
		UserEntity user = userService.findOrCreateOAuthUser(provider, providerId, email, name, picture);
		String accessToken = jwtService.generateAccessToken(user);

	    long expiresIn = jwtService.getAccessTokenExpiration();

	    return AuthResponse.builder()
	            .accessToken(accessToken)
	            .tokenType("Bearer")
	            .expiresIn(expiresIn)
	            .build();
	}

}
