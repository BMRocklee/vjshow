package com.vjshow.marketplace.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WalletResponse {
    private Long availableBalance;
    private Long pendingBalance;
    private Long totalEarned;
}
