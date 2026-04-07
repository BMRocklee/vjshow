package com.vjshow.marketplace.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePaymentMethodRequest {
    private String name;
    private String bankName;
    private String accountNumber;
}