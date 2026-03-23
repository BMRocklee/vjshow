package com.vjshow.marketplace.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.vjshow.marketplace.dto.response.ErrorResponseDto;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(LogicException.class)
	public ResponseEntity<ErrorResponseDto> handleLogicException(LogicException ex) {

		ErrorResponseDto res = ErrorResponseDto.builder().code(ex.getCode()).message(ex.getMessage())
				.timestamp(System.currentTimeMillis()).build();

		return ResponseEntity.badRequest().body(res);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponseDto> handleException(Exception ex) {

		ErrorResponseDto res = ErrorResponseDto.builder().code("INTERNAL_ERROR").message("Có lỗi xảy ra")
				.timestamp(System.currentTimeMillis()).build();

		return ResponseEntity.status(500).body(res);
	}
}