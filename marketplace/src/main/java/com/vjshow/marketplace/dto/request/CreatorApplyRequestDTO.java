package com.vjshow.marketplace.dto.request;

public class CreatorApplyRequestDTO {
	public record CreatorApplyRequest(
	        String name,
	        String email,
	        String phone,
	        String type,
	        String company
	) {}
}
