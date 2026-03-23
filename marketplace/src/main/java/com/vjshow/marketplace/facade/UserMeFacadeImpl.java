package com.vjshow.marketplace.facade;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.vjshow.marketplace.dto.response.UserMeResponseDto;
import com.vjshow.marketplace.entity.UserEntity;

@Component
public class UserMeFacadeImpl implements UserMeFacade {

	@Override
	public UserMeResponseDto getUserMe(Authentication authentication) {
		if (authentication == null || !authentication.isAuthenticated()) {
			return null;
		}

		UserEntity userme = (UserEntity) authentication.getPrincipal();

		UserMeResponseDto userMeResponseDto = UserMeResponseDto.builder().id(userme.getPublicId().toString()).name(userme.getName())
				.email(userme.getEmail()).picture(userme.getPicture()).role(userme.getRole().name()).build();

		return userMeResponseDto;
	}

}
