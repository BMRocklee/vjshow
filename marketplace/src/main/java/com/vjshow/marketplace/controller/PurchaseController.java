package com.vjshow.marketplace.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vjshow.marketplace.entity.UserEntity;
import com.vjshow.marketplace.exception.LogicException;
import com.vjshow.marketplace.facade.PurchaseFacade;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PurchaseController {

	private final PurchaseFacade purchaseFacade;

	@GetMapping("/me/purchases")
	public ResponseEntity<?> getMyPurchases(Authentication auth) {

		if (auth == null || !auth.isAuthenticated()) {
			 throw new LogicException("Unauthenticated", "bạn chưa đăng nhập hoặc session đã hết hạn");
		}

		UserEntity userme = (UserEntity) auth.getPrincipal();

		return ResponseEntity.ok(purchaseFacade.getMyPurchases(userme.getId()));
	}

	@GetMapping("/orders/{orderId}/download")
	public ResponseEntity<?> download(@PathVariable Long orderId, Authentication auth) {

		if (auth == null || !auth.isAuthenticated()) {
			 throw new LogicException("Unauthenticated", "bạn chưa đăng nhập hoặc session đã hết hạn");
		}

		UserEntity userme = (UserEntity) auth.getPrincipal();

		return ResponseEntity.ok(purchaseFacade.downloadProduct(userme.getId(), orderId));
	}
}
