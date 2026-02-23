package com.vjshow.marketplace.service;

import org.springframework.stereotype.Service;

import com.vjshow.marketplace.entity.User;
import com.vjshow.marketplace.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	
	public User createUser(String email, String name, String password) {
		User user = User.builder().email(email).name(name).password(password).build();
		
		return userRepository.save(user);
	}
	
	public User getByEmail(String email) {
		return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
	}

}
