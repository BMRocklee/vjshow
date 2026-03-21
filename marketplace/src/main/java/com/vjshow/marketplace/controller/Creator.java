package com.vjshow.marketplace.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vjshow.marketplace.dto.request.CreatorApplyRequestDTO.CreatorApplyRequest;
import com.vjshow.marketplace.entity.User;

@RestController
@RequestMapping("/api/creator")
public class Creator {

	@PostMapping("/apply")
	public ResponseEntity<?> apply(Authentication authentication, @RequestBody CreatorApplyRequest req) {

		User user = (User) authentication.getPrincipal();

		System.out.println(req);

		return ResponseEntity.ok(Map.of("status", "success"));
	}
}
