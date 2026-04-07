package com.vjshow.marketplace.facade;

import java.util.UUID;

import org.springframework.security.core.Authentication;

import com.vjshow.marketplace.dto.request.CreatorApplyRequestDTO.CreatorApplyRequest;
import com.vjshow.marketplace.dto.response.CreatorResponse;
import com.vjshow.marketplace.dto.response.ProductPageResponse;
import com.vjshow.marketplace.enums.ProductTypeEnum;

public interface CreatorFacade {

	public CreatorResponse getCreator(UUID publicId);

	public ProductPageResponse getProducts(UUID publicId, ProductTypeEnum type, int page, int size);

	public void apply(Authentication authentication, CreatorApplyRequest req);
}
