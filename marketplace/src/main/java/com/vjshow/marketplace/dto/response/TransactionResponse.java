package com.vjshow.marketplace.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TransactionResponse {
    private String id;
    private Long amount;
    private String type;
    private String referenceId;
    private LocalDateTime createdAt;
}
