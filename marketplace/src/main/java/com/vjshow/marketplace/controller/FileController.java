package com.vjshow.marketplace.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vjshow.marketplace.dto.request.CompleteUploadRequest;
import com.vjshow.marketplace.dto.response.UploadResponse;
import com.vjshow.marketplace.entity.ProductEntity;
import com.vjshow.marketplace.entity.UserEntity;
import com.vjshow.marketplace.exception.LogicException;
import com.vjshow.marketplace.facade.UploadFacade;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/file/upload")
@RequiredArgsConstructor
public class FileController {
	
	private final UploadFacade uploadFacade;
	
	@PostMapping("/init")
	public UploadResponse initUpload(Authentication authentication, @RequestParam String fileName,
			@RequestParam Long fileSize) {

		if (authentication == null || !authentication.isAuthenticated()) {
			throw new LogicException("AUTHEN_FAIL", "Đăng nhập lại");
		}
		
		UserEntity currentUser = (UserEntity) authentication.getPrincipal();

		return uploadFacade.initUpload(fileName, fileSize, currentUser.getId());
	}

	@PostMapping("/complete")
	public ProductEntity completeUpload(Authentication authentication, @RequestBody CompleteUploadRequest request) {
		
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new LogicException("AUTHEN_FAIL", "Đăng nhập lại");
		}
		
		UserEntity currentUser = (UserEntity) authentication.getPrincipal();
		
		ProductEntity product = uploadFacade.completeUpload(request, currentUser.getId());
						
		return product;
	}
}
