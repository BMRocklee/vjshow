package com.vjshow.marketplace.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PurchaseItemResponse {
    private Long orderId;
    private Long productId;
    private String name;
    private String thumbnail;
    private Long price;
    private LocalDateTime purchasedAt;
    private boolean canDownload;
}
