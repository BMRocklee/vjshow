package com.vjshow.marketplace.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vjshow.marketplace.entity.CreatorEntity;
import com.vjshow.marketplace.entity.UserEntity;
import com.vjshow.marketplace.facade.WalletFacade;
import com.vjshow.marketplace.service.CreatorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/creator/wallet")
@RequiredArgsConstructor
public class CreatorWalletController {

	private final WalletFacade facade;

	private final CreatorService creatorService;

	@GetMapping("/me")
	public ResponseEntity<?> getWallet(Authentication auth) {
		UserEntity userme = (UserEntity) auth.getPrincipal();
		CreatorEntity creator = creatorService.getByUserId(userme.getId());
		
		return ResponseEntity.ok(facade.getMyWallet(creator.getId()));
	}

	@GetMapping("/transactions")
	public ResponseEntity<?> getTransactions(Authentication auth) {
		UserEntity userme = (UserEntity) auth.getPrincipal();
		CreatorEntity creator = creatorService.getByUserId(userme.getId());
		return ResponseEntity.ok(facade.getMyTransactions(creator.getId()));
	}

	@GetMapping("/withdraws")
	public ResponseEntity<?> getWithdraws(Authentication auth) {
		UserEntity userme = (UserEntity) auth.getPrincipal();
		CreatorEntity creator = creatorService.getByUserId(userme.getId());
		return ResponseEntity.ok(facade.getMyWithdraws(creator.getId()));
	}
}
