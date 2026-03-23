package com.vjshow.marketplace.facade;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.vjshow.marketplace.dto.request.CreatorApplyRequestDTO.CreatorApplyRequest;
import com.vjshow.marketplace.entity.UserEntity;
import com.vjshow.marketplace.exception.LogicException;
import com.vjshow.marketplace.service.CreatorService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreatorFacadeImpl implements CreatorFacade {
	
	private final CreatorService creatorService;

	@Override
	public void apply(Authentication authentication, CreatorApplyRequest req) {
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new LogicException("UNAUTHORIZED", "authen fail cần đăng nhập lại");
		}

		UserEntity userme = (UserEntity) authentication.getPrincipal();
		creatorService.apply(userme, req);
	}

}
