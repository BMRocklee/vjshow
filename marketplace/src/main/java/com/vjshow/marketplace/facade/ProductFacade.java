package com.vjshow.marketplace.facade;

import java.util.List;

import org.springframework.data.domain.Page;

import com.vjshow.marketplace.dto.request.HandleFileDoneRequest;
import com.vjshow.marketplace.dto.response.UserProductResponse;
import com.vjshow.marketplace.enums.ProductTypeEnum;

public interface ProductFacade {
	public Page<UserProductResponse> getPublicProducts(String type, String keyword, int pagee, int size);
	
	public UserProductResponse getById(Long id);
	
	void markDone(HandleFileDoneRequest request);

	public List<UserProductResponse> getTopProducts(ProductTypeEnum type, Long quantity);
}
