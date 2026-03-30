package com.vjshow.marketplace.facade;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.vjshow.marketplace.dto.request.ProductRequest;
import com.vjshow.marketplace.dto.response.UserMeResponseDto;
import com.vjshow.marketplace.dto.response.UserProductResponse;
import com.vjshow.marketplace.entity.ProductEntity;

public interface UserMeFacade {
	public UserMeResponseDto getUserMe(Authentication authentication);
	
	public List<UserProductResponse> getMyProducts(Authentication authentication);

	public ProductEntity updateMyProduct(Long id, ProductRequest request);

	public void deleteMyProduct(Long id);
}
