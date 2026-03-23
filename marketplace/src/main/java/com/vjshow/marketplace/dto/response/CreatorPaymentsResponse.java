package com.vjshow.marketplace.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreatorPaymentsResponse {
	
	  private CreatorStatsDTO stats;
	  private List<CreatorPaymentInfoDTO> creators;
}
