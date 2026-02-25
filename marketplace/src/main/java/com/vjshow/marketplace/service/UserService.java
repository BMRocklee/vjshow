package com.vjshow.marketplace.service;

import com.vjshow.marketplace.entity.User;
import com.vjshow.marketplace.enums.AuthProviderEnum;

public interface UserService {
	public User findOrCreateOAuthUser(AuthProviderEnum provider, String providerId, String email, String name, String picture);
}
