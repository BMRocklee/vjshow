package com.vjshow.marketplace.dto.request;

import java.util.List;

import lombok.Data;

@Data
public class CassoWebhookDto {
	private List<CassoTransaction> data;
	
	@Data
	public class CassoTransaction {
	    private String description;
	    private Long amount;
	    private String tid;
	}
}
