package com.vjshow.marketplace.service;

import java.util.UUID;

import com.vjshow.marketplace.entity.UserEntity;

public interface JwtService {
	public String generateAccessToken(UserEntity user);
	
	public UUID extractUserId(String token);
	
	public boolean isValid(String token);
	
	public long getAccessTokenExpiration();
}
