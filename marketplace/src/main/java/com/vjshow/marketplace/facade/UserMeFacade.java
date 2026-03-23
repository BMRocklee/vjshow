package com.vjshow.marketplace.facade;

import org.springframework.security.core.Authentication;

import com.vjshow.marketplace.dto.response.UserMeResponseDto;

public interface UserMeFacade {
	public UserMeResponseDto getUserMe(Authentication authentication);

}
