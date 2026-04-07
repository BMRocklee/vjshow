package com.vjshow.marketplace.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductPageResponse {
	private List<UserProductResponse> items;
    private Long total;
}
