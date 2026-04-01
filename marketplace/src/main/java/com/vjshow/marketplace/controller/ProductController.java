package com.vjshow.marketplace.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vjshow.marketplace.dto.request.HandleFileDoneRequest;
import com.vjshow.marketplace.enums.ProductTypeEnum;
import com.vjshow.marketplace.facade.ProductFacade;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

	private final ProductFacade productFacade;

	@GetMapping
	public ResponseEntity<?> getProducts(@RequestParam(required = false) String type,
			@RequestParam(required = false, defaultValue = "") String keyword) {
		return ResponseEntity.ok(productFacade.getProducts(type, keyword));
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Long id) {
		return ResponseEntity.ok(productFacade.getById(id));
	}

	@PostMapping("/done")
	public void done(@RequestBody HandleFileDoneRequest fileInfo) {
		productFacade.markDone(fileInfo);
	}

	@GetMapping("/getTop")
	public ResponseEntity<?> getTopProducts(@RequestParam ProductTypeEnum type, @RequestParam Long quantity) {
		return ResponseEntity.ok(productFacade.getTopProducts(type, quantity));
	}
}
