package com.vjshow.marketplace.service;

import com.vjshow.marketplace.dto.request.CompleteUploadRequest;
import com.vjshow.marketplace.entity.CreatorEntity;
import com.vjshow.marketplace.entity.ProductEntity;

public interface ProductService {

	public ProductEntity getById(Long id);
	
	ProductEntity createProduct(CompleteUploadRequest request, CreatorEntity creator);
}
