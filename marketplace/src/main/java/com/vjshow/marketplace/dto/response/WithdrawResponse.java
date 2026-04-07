package com.vjshow.marketplace.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WithdrawResponse {
    private String id;
    private Long amount;
    private String status;
    private String bankInfo;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
} 