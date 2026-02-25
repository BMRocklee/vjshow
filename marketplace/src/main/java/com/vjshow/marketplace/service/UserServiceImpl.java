package com.vjshow.marketplace.service;

import org.springframework.stereotype.Component;

import com.vjshow.marketplace.entity.User;
import com.vjshow.marketplace.enums.AuthProviderEnum;
import com.vjshow.marketplace.enums.Role;
import com.vjshow.marketplace.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Override
	public User findOrCreateOAuthUser(AuthProviderEnum provider, String providerId, String email, String name,
			String picture) {
		return userRepository.findByProviderAndProviderId(provider, providerId)
				.orElseGet(() -> createUser(provider, providerId, email, name, picture));
	}

	private User createUser(AuthProviderEnum provider, String providerId, String email, String name, String picture) {
		User user = User.builder().provider(provider).providerId(providerId).email(email).name(name).picture(picture)
				.role(Role.USER).build();
		return userRepository.save(user);
	}

}
