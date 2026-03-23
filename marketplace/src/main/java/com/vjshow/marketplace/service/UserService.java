package com.vjshow.marketplace.service;

import com.vjshow.marketplace.entity.UserEntity;
import com.vjshow.marketplace.enums.AuthProviderEnum;

public interface UserService {
	public UserEntity findOrCreateOAuthUser(AuthProviderEnum provider, String providerId, String email, String name, String picture);
	
	public long getTotalUsers();
}
