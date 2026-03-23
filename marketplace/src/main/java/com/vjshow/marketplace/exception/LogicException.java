package com.vjshow.marketplace.exception;

import lombok.Getter;

@Getter
public class LogicException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String code;
    private final String message;

    public LogicException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}