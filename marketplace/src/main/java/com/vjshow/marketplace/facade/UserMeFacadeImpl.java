package com.vjshow.marketplace.facade;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.vjshow.marketplace.dto.request.ProductRequest;
import com.vjshow.marketplace.dto.response.UserMeResponseDto;
import com.vjshow.marketplace.dto.response.UserProductResponse;
import com.vjshow.marketplace.entity.CreatorEntity;
import com.vjshow.marketplace.entity.ProductEntity;
import com.vjshow.marketplace.entity.UserEntity;
import com.vjshow.marketplace.exception.LogicException;
import com.vjshow.marketplace.mapper.ProductMapper;
import com.vjshow.marketplace.service.CreatorService;
import com.vjshow.marketplace.service.ProductService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserMeFacadeImpl implements UserMeFacade {
	
	private final ProductService productService;
    private final CreatorService creatorService;
    private final ProductMapper productMapper;

	@Override
	public UserMeResponseDto getUserMe(Authentication authentication) {
		if (authentication == null || !authentication.isAuthenticated()) {
			 throw new LogicException("Unauthenticated", "bạn chưa đăng nhập hoặc session đã hết hạn");
		}

		UserEntity userme = (UserEntity) authentication.getPrincipal();

		UserMeResponseDto userMeResponseDto = UserMeResponseDto.builder().id(userme.getPublicId().toString()).name(userme.getName())
				.email(userme.getEmail()).picture(userme.getPicture()).role(userme.getRole().name()).build();

		return userMeResponseDto;
	}

	@Override
	public List<UserProductResponse> getMyProducts(Authentication authentication) {
		if (authentication == null || !authentication.isAuthenticated()) {
			 throw new LogicException("Unauthenticated", "bạn chưa đăng nhập hoặc session đã hết hạn");
		}

		UserEntity userme = (UserEntity) authentication.getPrincipal();
		CreatorEntity creator =  creatorService.getByUserId(userme.getId());
        var products = productService.getByCreatorId(creator.getId());
		
        return products.stream()
                .map(productMapper::toResponse)
                .toList();
	}

	@Override
	public ProductEntity updateMyProduct(Long id, ProductRequest request) {
		return productService.update(id, request);
	}

	@Override
	public void deleteMyProduct(Long id) {
		productService.delete(id);
	}

}
