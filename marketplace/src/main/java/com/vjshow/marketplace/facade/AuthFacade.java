package com.vjshow.marketplace.facade;

import com.vjshow.marketplace.dto.response.AuthResponse;
import com.vjshow.marketplace.enums.AuthProviderEnum;

public interface AuthFacade {
	public AuthResponse loginOAuth(AuthProviderEnum provider, String providerId, String email, String name, String picture);
}
