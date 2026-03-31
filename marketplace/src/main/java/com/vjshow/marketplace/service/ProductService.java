package com.vjshow.marketplace.service;

import java.util.List;

import com.vjshow.marketplace.dto.request.CompleteUploadRequest;
import com.vjshow.marketplace.dto.request.ProductRequest;
import com.vjshow.marketplace.entity.CreatorEntity;
import com.vjshow.marketplace.entity.ProductEntity;

public interface ProductService {

	public ProductEntity getById(Long id);
	
	public ProductEntity createProduct(CompleteUploadRequest request, CreatorEntity creator);
	
	public List<ProductEntity> getByCreatorId(Long creatorId);
    
	public ProductEntity update(Long id, ProductRequest request);
    
	public void delete(Long id);
	
    List<ProductEntity> getPublicProducts(String type, String keyword);
}
