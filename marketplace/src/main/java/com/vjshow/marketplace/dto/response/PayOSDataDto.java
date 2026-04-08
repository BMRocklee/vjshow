package com.vjshow.marketplace.dto.response;

import lombok.Data;

@Data
public class PayOSDataDto {

    private Long orderCode;
    private Long amount;
    private String description;
    private String reference;
    private String transactionDateTime;

    private String accountNumber;
    private String currency;
    private String paymentLinkId;

    // 👇 QUAN TRỌNG
    private String code;
    private String desc;

    private String counterAccountBankId;
    private String counterAccountBankName;
    private String counterAccountName;
    private String counterAccountNumber;

    private String virtualAccountName;
    private String virtualAccountNumber;
}
