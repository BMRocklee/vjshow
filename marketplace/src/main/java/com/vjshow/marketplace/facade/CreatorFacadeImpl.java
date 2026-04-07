package com.vjshow.marketplace.facade;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.vjshow.marketplace.dto.request.CreatorApplyRequestDTO.CreatorApplyRequest;
import com.vjshow.marketplace.dto.response.CreatorResponse;
import com.vjshow.marketplace.dto.response.ProductPageResponse;
import com.vjshow.marketplace.entity.UserEntity;
import com.vjshow.marketplace.enums.ProductTypeEnum;
import com.vjshow.marketplace.exception.LogicException;
import com.vjshow.marketplace.service.CreatorService;
import com.vjshow.marketplace.service.ProductService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreatorFacadeImpl implements CreatorFacade {
	
	private final CreatorService creatorService;
	
	private final ProductService productService;

	@Override
	public CreatorResponse getCreator(UUID publicId) {
		return creatorService.getByPublicId(publicId);
	}

	@Override
	public ProductPageResponse getProducts(UUID publicId, ProductTypeEnum type, int page, int size) {
		// validate type nếu cần
        if (type == null) {
            type = ProductTypeEnum.IMAGE; // default giống FE
        }

        return productService.getByCreator(publicId, type, page, size);
	}

	@Override
	public void apply(Authentication authentication, CreatorApplyRequest req) {
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new LogicException("UNAUTHORIZED", "authen fail cần đăng nhập lại");
		}

		UserEntity userme = (UserEntity) authentication.getPrincipal();
		creatorService.apply(userme, req);
		
	}

}
