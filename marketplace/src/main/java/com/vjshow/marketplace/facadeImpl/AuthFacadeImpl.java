package com.vjshow.marketplace.facadeImpl;

import org.springframework.stereotype.Component;

import com.vjshow.marketplace.dto.response.AuthResponse;
import com.vjshow.marketplace.enums.AuthProviderEnum;
import com.vjshow.marketplace.facade.AuthFacade;
import com.vjshow.marketplace.service.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthFacadeImpl implements AuthFacade {
	
//    private final JwtService jwtService;
	
	private final UserService userService;

	@Override
	public AuthResponse loginOAuth(String provider, String providerId, String email, String name, String picture) {
		// TODO Auto-generated method stub
		AuthProviderEnum authProvider = AuthProviderEnum.valueOf(provider.toUpperCase());
		
//		UserService user = userService.findOrCreateOAuthUser(authProvider, providerId, email, name, picture);
//        return jwtService.generateToken(user);
		return null;
	}

}
