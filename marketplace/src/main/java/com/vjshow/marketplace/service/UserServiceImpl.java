package com.vjshow.marketplace.service;

import org.springframework.stereotype.Component;

import com.vjshow.marketplace.entity.UserEntity;
import com.vjshow.marketplace.enums.AuthProviderEnum;
import com.vjshow.marketplace.enums.Role;
import com.vjshow.marketplace.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Override
	public UserEntity findOrCreateOAuthUser(AuthProviderEnum provider, String providerId, String email, String name,
			String picture) {
		return userRepository.findByProviderAndProviderId(provider, providerId)
				.orElseGet(() -> createUser(provider, providerId, email, name, picture));
	}

	private UserEntity createUser(AuthProviderEnum provider, String providerId, String email, String name, String picture) {
		UserEntity user = UserEntity.builder().provider(provider).providerId(providerId).email(email).name(name).picture(picture)
				.role(Role.USER).build();
		return userRepository.save(user);
	}

	@Override
	public long getTotalUsers() {
		return userRepository.count();
	}

}
