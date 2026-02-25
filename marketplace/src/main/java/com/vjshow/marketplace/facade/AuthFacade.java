package com.vjshow.marketplace.facade;

import com.vjshow.marketplace.dto.response.AuthResponse;

public interface AuthFacade {
	public AuthResponse loginOAuth(String provider, String providerId, String email, String name, String picture);
}
