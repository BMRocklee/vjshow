package com.vjshow.marketplace.dto.response;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OwnerResponse {
	private Long id;
    private UUID publicId;
    private String name;
    private String picture;
}
