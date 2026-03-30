package com.vjshow.marketplace.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vjshow.marketplace.dto.request.ProductRequest;
import com.vjshow.marketplace.dto.response.UserMeResponseDto;
import com.vjshow.marketplace.dto.response.UserProductResponse;
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
	
	@GetMapping("/me/products")
	public ResponseEntity<?> getMyProduct(Authentication authentication) {

		List<UserProductResponse> listProducts = userMeFacade.getMyProducts(authentication);

		return ResponseEntity.ok(listProducts);

	}
	
	@PutMapping("/me/products/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ProductRequest request) {
		return ResponseEntity.ok(userMeFacade.updateMyProduct(id, request));
	}
	
	@DeleteMapping("/me/products/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
		userMeFacade.deleteMyProduct(id);
        return ResponseEntity.ok().build();
    }
	
	
}
