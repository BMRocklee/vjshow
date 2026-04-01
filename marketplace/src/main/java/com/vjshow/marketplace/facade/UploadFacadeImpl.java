package com.vjshow.marketplace.facade;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.vjshow.marketplace.dto.request.CompleteUploadRequest;
import com.vjshow.marketplace.dto.response.UploadResponse;
import com.vjshow.marketplace.entity.CreatorEntity;
import com.vjshow.marketplace.entity.ProductEntity;
import com.vjshow.marketplace.service.CloudFlareService;
import com.vjshow.marketplace.service.CreatorService;
import com.vjshow.marketplace.service.ProductService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UploadFacadeImpl implements UploadFacade{
	
	private final CreatorService creatorService;
	
	private final CloudFlareService r2Service;
	
	private final ProductService productService;
	
	 @Override
	    public UploadResponse initUpload(String fileName, Long fileSize, Long userId) {

	        CreatorEntity creator = creatorService.getByUserId(userId);

	        creatorService.checkQuota(creator, fileSize);

	        String key = "original/" + userId + "/" + UUID.randomUUID() + "_" + fileName;

	        String presignedUrl = r2Service.generatePresignedUrl(key);

	        return new UploadResponse(key, presignedUrl);
	    }

	    @Override
	    public ProductEntity completeUpload(CompleteUploadRequest request, Long userId) {

	    	CreatorEntity creator = creatorService.getByUserId(userId);

	        creatorService.checkQuota(creator, request.getFileSize());

	        ProductEntity product = productService.createProduct(request, creator);

	        creatorService.increaseStorage(creator, request.getFileSize());

	        return product;
	    }
}
