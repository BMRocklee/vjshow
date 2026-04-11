package com.vjshow.marketplace.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vjshow.marketplace.dto.request.HandleFileDoneRequest;
import com.vjshow.marketplace.dto.response.UserProductResponse;
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
			@RequestParam(required = false, defaultValue = "") String keyword,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "12") int size) {

		Page<UserProductResponse> data = productFacade.getPublicProducts(type, keyword, page, size);
		Map<String, Object> res = new HashMap<>();
		res.put("content", data.getContent());
		res.put("totalPages", data.getTotalPages());
		res.put("totalElements", data.getTotalElements());

		return ResponseEntity.ok(res);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Long id) {
		return ResponseEntity.ok(productFacade.getById(id));
	}

	@PostMapping("/done")
	public ResponseEntity<?> done(@RequestBody HandleFileDoneRequest fileInfo) {
		boolean status = productFacade.markDone(fileInfo);
		if (status) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.badRequest().body("file upload đã tồn tại!");
		}
	}

	@GetMapping("/getTop")
	public ResponseEntity<?> getTopProducts(@RequestParam ProductTypeEnum type,
			@RequestParam(defaultValue = "0") int page, 
			@RequestParam(defaultValue = "5") int size) {
		return ResponseEntity.ok(productFacade.getTopProducts(type, page, size));
	}
}
