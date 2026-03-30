package com.vjshow.marketplace.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vjshow.marketplace.dto.request.HandleFileDoneRequest;
import com.vjshow.marketplace.entity.ProductEntity;
import com.vjshow.marketplace.enums.ProductStatusEnum;
import com.vjshow.marketplace.enums.ProductTypeEnum;
import com.vjshow.marketplace.exception.LogicException;
import com.vjshow.marketplace.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

	private final ProductRepository productRepository;
	
	@GetMapping
	public List<ProductEntity> getProducts(@RequestParam(required = false) String type,
			@RequestParam(required = false, defaultValue = "") String keyword) {
		if (type == null || type.isEmpty()) {
			return productRepository.findByNameContainingIgnoreCase(keyword);
		}

		return productRepository.findByTypeAndNameContainingIgnoreCase(ProductTypeEnum.valueOf(type), keyword);
	}
	
	@GetMapping("/{id}")
	public ProductEntity getById(@PathVariable Long id) {
	    return productRepository.findById(id)
	        .orElseThrow(() -> new LogicException("NOT_FOUND","Không tìm thấy sản phẩm"));
	}

	@PostMapping("/done")
	public void done(@RequestBody HandleFileDoneRequest fileInfo) {
		String fileKey = fileInfo.getFileKey();

		ProductEntity product = productRepository.findByFileKey(fileKey);

		product.setPreviewUrl(fileInfo.getPreviewUrl());
		product.setThumbnailUrl(fileInfo.getThumbUrl());
		product.setWidth(fileInfo.getWidth());
		product.setHeight(fileInfo.getHeight());
		product.setDuration(fileInfo.getDuration());
		
		product.setStatus(ProductStatusEnum.DONE);
		productRepository.save(product);
	}
}
