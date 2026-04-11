package com.vjshow.marketplace.facade;

import org.springframework.data.domain.Page;

import com.vjshow.marketplace.dto.request.HandleFileDoneRequest;
import com.vjshow.marketplace.dto.response.PageResponse;
import com.vjshow.marketplace.dto.response.UserProductResponse;
import com.vjshow.marketplace.enums.ProductTypeEnum;

public interface ProductFacade {
	public Page<UserProductResponse> getPublicProducts(String type, String keyword, int pagee, int size);
	
	public UserProductResponse getById(Long id);
	
	public boolean markDone(HandleFileDoneRequest request);

	public PageResponse<UserProductResponse> getTopProducts(ProductTypeEnum type, int page, int size);
}
