package com.vjshow.marketplace.service;

import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.vjshow.marketplace.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtServiceImpl implements JwtService {

	private final String SECRET = "12345678901234567890123456789012";

	private static final long EXPIRATION = 86400000; // 24h

	private SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());
	
	@Override
	public String generateAccessToken(User user) {

		Date now = new Date();
		Date expiry = new Date(now.getTime() + EXPIRATION);
		return Jwts.builder().subject(user.getPublicId().toString()).claim("email", user.getEmail())
				.claim("role", user.getRole().name()).issuedAt(now).expiration(expiry).signWith(key).compact();
	}

	private Claims parse(String token) {

		return Jwts.parser()

				.verifyWith(key)

				.build()

				.parseSignedClaims(token)

				.getPayload();

	}

	@Override
	public UUID extractUserId(String token) {
		Claims claims = parse(token);
		return UUID.fromString(claims.getSubject());
	}

	@Override
	public boolean isValid(String token) {
		try {
			parse(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
