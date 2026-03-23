package com.vjshow.marketplace.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AdminDashboardResponseDto {
	private long totalUsers;
	private long totalCreators;
	private long totalCreatorsPending;
}
