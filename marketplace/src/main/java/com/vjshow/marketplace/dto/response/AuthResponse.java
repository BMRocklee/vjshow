package com.vjshow.marketplace.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {

    private String accessToken;

    private String tokenType;

    private long expiresIn;

}