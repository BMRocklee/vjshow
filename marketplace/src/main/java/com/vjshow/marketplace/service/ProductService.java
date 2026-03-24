package com.vjshow.marketplace.service;

import org.springframework.stereotype.Service;

import com.vjshow.marketplace.entity.ProductEntity;
import com.vjshow.marketplace.exception.LogicException;
import com.vjshow.marketplace.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepo;

	public ProductEntity getById(Long id) {
		return productRepo.findById(id).orElseThrow(() -> new LogicException("NOT_FOUND", "không tồn tại sản phẩm"));
	}
}
