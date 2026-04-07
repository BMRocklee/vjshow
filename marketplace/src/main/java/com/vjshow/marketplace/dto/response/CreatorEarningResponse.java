package com.vjshow.marketplace.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreatorEarningResponse {
    private Long totalRevenue;
    private Long systemCommission;
    private Long creatorRevenue;
    private Long withdrawable;
}