package com.vjshow.marketplace.service;

import com.vjshow.marketplace.dto.response.UserAdminResponse;
import com.vjshow.marketplace.entity.UserEntity;
import com.vjshow.marketplace.enums.AuthProviderEnum;
import com.vjshow.marketplace.enums.UserStatusEnum;

public interface UserService {
	public UserEntity findOrCreateOAuthUser(AuthProviderEnum provider, String providerId, String email, String name, String picture);
	
	public long getTotalUsers();
	
	public UserAdminResponse  getUsers(String keyword, UserStatusEnum status);
}
