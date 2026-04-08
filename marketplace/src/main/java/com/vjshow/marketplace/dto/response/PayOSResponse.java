package com.vjshow.marketplace.dto.response;

import lombok.Data;

@Data
public class PayOSResponse {
	private String code;
	private String desc;
	private Data data;

	@lombok.Data
	public static class Data {
		private String checkoutUrl;
	}
}
