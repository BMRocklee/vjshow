package com.vjshow.marketplace.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vjshow.marketplace.dto.response.UserMeResponseDto;
import com.vjshow.marketplace.facade.UserMeFacade;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserMeController {

	private final UserMeFacade userMeFacade;

	@GetMapping("/me")
	public ResponseEntity<?> getUserMe(Authentication authentication) {

		UserMeResponseDto userMe = userMeFacade.getUserMe(authentication);

		if (ObjectUtils.isEmpty(userMe)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Chưa đăng nhập");
		}

		return ResponseEntity.ok(userMe);

	}
}