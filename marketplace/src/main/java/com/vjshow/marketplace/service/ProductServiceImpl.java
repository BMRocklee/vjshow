package com.vjshow.marketplace.service;

import org.springframework.stereotype.Component;

import com.vjshow.marketplace.dto.request.CompleteUploadRequest;
import com.vjshow.marketplace.entity.CreatorEntity;
import com.vjshow.marketplace.entity.ProductEntity;
import com.vjshow.marketplace.enums.ProductStatusEnum;
import com.vjshow.marketplace.enums.ProductTypeEnum;
import com.vjshow.marketplace.exception.LogicException;
import com.vjshow.marketplace.repository.ProductRepository;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
	
	private final ProductRepository productRepository;

	public ProductEntity getById(Long id) {
		return productRepository.findById(id).orElseThrow(() -> new LogicException("NOT_FOUND", "không tồn tại sản phẩm"));
	}

	@Override
	public ProductEntity createProduct(CompleteUploadRequest request, CreatorEntity creator) {
		 ProductEntity product = ProductEntity.builder()
	                .name(request.getName())
	                .description(request.getDescription())
	                .type(ProductTypeEnum.valueOf(request.getType()))
	                .fileKey(request.getKey())
	                .price(request.getPrice())
	                .status(ProductStatusEnum.UPLOADED)
	                .duration(request.getDuration())
	                .creator(creator)
	                .format(request.getContentType())
	                .build();
		 
		 return productRepository.save(product);
	}
}
