package com.vjshow.marketplace.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vjshow.marketplace.config.CustomUserDetails;
import com.vjshow.marketplace.dto.request.CreatePaymentMethodRequest;
import com.vjshow.marketplace.dto.request.CreatorApplyRequestDTO.CreatorApplyRequest;
import com.vjshow.marketplace.dto.request.WithdrawRequestDto;
import com.vjshow.marketplace.dto.response.CommonResponseDto;
import com.vjshow.marketplace.dto.response.CreatorResponse;
import com.vjshow.marketplace.dto.response.ProductPageResponse;
import com.vjshow.marketplace.entity.CreatorEntity;
import com.vjshow.marketplace.entity.UserEntity;
import com.vjshow.marketplace.enums.ProductTypeEnum;
import com.vjshow.marketplace.exception.LogicException;
import com.vjshow.marketplace.facade.CreatorFacade;
import com.vjshow.marketplace.facade.WalletFacade;
import com.vjshow.marketplace.service.CreatorService;
import com.vjshow.marketplace.service.PaymentMethodService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/creator")
@RequiredArgsConstructor
public class CreatorController {

	private final CreatorFacade creatorFacade;

	private final WalletFacade facade;

	private final CreatorService creatorService;

	private final PaymentMethodService service;

	@PostMapping("/apply")
	public ResponseEntity<?> apply(Authentication authentication, @RequestBody CreatorApplyRequest req) {
		creatorFacade.apply(authentication, req);
		CommonResponseDto res = CommonResponseDto.builder()
				.message("gửi đăng kí thành công!bạn hãy kiên nhẫn chờ xét duyệt nhé").build();
		return ResponseEntity.ok(res);
	}

	@GetMapping("/{publicId}")
	public CreatorResponse getCreator(@PathVariable UUID publicId) {
		return creatorFacade.getCreator(publicId);
	}

	@GetMapping("/{publicId}/products")
	public ProductPageResponse getProducts(@PathVariable("publicId") UUID publicId,
			@RequestParam(defaultValue = "IMAGE") ProductTypeEnum type, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size) {

		return creatorFacade.getProducts(publicId, type, page, size);
	}

	@PostMapping("/withdraw")
	public ResponseEntity<?> withdraw(Authentication auth, @RequestBody WithdrawRequestDto dto) {

		if (auth == null || !auth.isAuthenticated()) {
			throw new LogicException("Unauthenticated", "bạn chưa đăng nhập hoặc session đã hết hạn");
		}

		UserEntity userme = (UserEntity) auth.getPrincipal();

		CreatorEntity creator = creatorService.getByUserId(userme.getId());

		facade.requestWithdraw(creator.getId(), dto.getAmount(), dto.getBankInfo());

		return ResponseEntity.ok().build();
	}

	@PostMapping("/payment-method")
	public ResponseEntity<?> create(Authentication authentication, @RequestBody CreatePaymentMethodRequest request) {
		UserEntity userme = (UserEntity) authentication.getPrincipal();

		CreatorEntity creator = creatorService.getByUserId(userme.getId());
		service.create(creator, request);

		return ResponseEntity.ok().build();
	}
}
