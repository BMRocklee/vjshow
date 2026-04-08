package com.vjshow.marketplace.dto.response;

import lombok.Data;

@Data
public class PayOSWebhookDto {
    private String code;
    private String desc;
    private Boolean success;
    private PayOSDataDto data;
    private String signature;
}
