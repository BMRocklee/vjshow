package com.vjshow.marketplace.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vjshow.marketplace.entity.User;
import com.vjshow.marketplace.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
	private UserService userService;

	@PostMapping
	private User createUser(@RequestParam String email, @RequestParam String name, @RequestParam String password) {
		// TODO Auto-generated method stub
		return userService.createUser(email, name, password);
	}

	@GetMapping("/by-email")
	public User getByEmail(@RequestParam String email) {
		return userService.getByEmail(email);
	}
}
