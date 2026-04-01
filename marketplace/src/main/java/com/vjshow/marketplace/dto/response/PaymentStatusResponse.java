package com.vjshow.marketplace.dto.response;

import com.vjshow.marketplace.enums.PaymentStatusEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatusResponse {
	private PaymentStatusEnum status;
    private String downloadUrl;
}
