package com.vjshow.marketplace.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserStatsDTO {
    private long totalUsers;
    private long active;
    private long blocked;
    private double totalSpent;
}
