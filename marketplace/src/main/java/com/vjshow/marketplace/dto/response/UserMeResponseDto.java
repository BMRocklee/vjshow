package com.vjshow.marketplace.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserMeResponseDto {

	private String id;
    private String name;
    private String email;
    private String picture;
    private String role;
}
