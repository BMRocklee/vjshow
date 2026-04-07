package com.vjshow.marketplace.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawRequestDto {
	
    private Long amount;

    private String bankInfo; 
    // ví dụ: "VCB - 123456789 - NGUYEN VAN A"
}
