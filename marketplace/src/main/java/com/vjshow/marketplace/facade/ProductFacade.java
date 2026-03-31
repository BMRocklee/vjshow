package com.vjshow.marketplace.facade;

import java.util.List;

import com.vjshow.marketplace.dto.request.HandleFileDoneRequest;
import com.vjshow.marketplace.dto.response.UserProductResponse;

public interface ProductFacade {
	public List<UserProductResponse> getProducts(String type, String keyword);
	
	public UserProductResponse getById(Long id);
	
	void markDone(HandleFileDoneRequest request);
}
