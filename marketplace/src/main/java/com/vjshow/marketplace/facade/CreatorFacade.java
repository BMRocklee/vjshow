package com.vjshow.marketplace.facade;

import org.springframework.security.core.Authentication;

import com.vjshow.marketplace.dto.request.CreatorApplyRequestDTO.CreatorApplyRequest;

public interface CreatorFacade {
	public void apply(Authentication authentication, CreatorApplyRequest req);
}
