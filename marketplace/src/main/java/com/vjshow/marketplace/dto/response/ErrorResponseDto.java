package com.vjshow.marketplace.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponseDto {
	private String code;
	private String message;
	private long timestamp;
}
