package com.vjshow.marketplace.service;

import java.util.UUID;

import com.vjshow.marketplace.entity.User;

public interface JwtService {
	public String generateAccessToken(User user);
	
	public UUID extractUserId(String token);
	
	public boolean isValid(String token);
	
	public long getAccessTokenExpiration();
}
