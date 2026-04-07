package com.vjshow.marketplace.dto.response;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatorResponse {
	private UUID publicId;
    private String name;
    private String picture;
    private Boolean verified;
}
