package com.vjshow.marketplace.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatorStatsDTO {
    private long totalCreators;
    private long totalApprovedCreators;
    private long totalPendingCreators;
    private long totalRejectCreators;
    private long totalRevenue;
    private long totalCommission;
}
