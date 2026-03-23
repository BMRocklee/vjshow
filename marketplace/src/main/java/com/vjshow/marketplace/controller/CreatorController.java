package com.vjshow.marketplace.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vjshow.marketplace.dto.request.CreatorApplyRequestDTO.CreatorApplyRequest;
import com.vjshow.marketplace.dto.response.CommonResponseDto;
import com.vjshow.marketplace.facade.CreatorFacade;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/creator")
@RequiredArgsConstructor
public class CreatorController {

	private final CreatorFacade creatorFacade;

	@PostMapping("/apply")
	public ResponseEntity<?> apply(Authentication authentication, @RequestBody CreatorApplyRequest req) {
		creatorFacade.apply(authentication, req);
		CommonResponseDto res = CommonResponseDto.builder()
				.message("gửi đăng kí thành công!bạn hãy kiên nhẫn chờ xét duyệt nhé").build();
		return ResponseEntity.ok(res);
	}
}
