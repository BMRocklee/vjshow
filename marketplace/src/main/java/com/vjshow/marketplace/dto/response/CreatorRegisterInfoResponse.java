package com.vjshow.marketplace.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatorRegisterInfoResponse {
	
	  private CreatorStatsDTO stats;
	  private List<CreatorAdminResponseDTO> creators;
}
