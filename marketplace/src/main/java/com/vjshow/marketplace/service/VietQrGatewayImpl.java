package com.vjshow.marketplace.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.vjshow.marketplace.dto.response.PayOSResponse;
import com.vjshow.marketplace.entity.PaymentEntity;

import vn.payos.PayOS;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;

@Service
public class VietQrGatewayImpl implements PaymentGatewayService {

	@Value("${payos.client-id}")
	private String clientId;

	@Value("${payos.api-key}")
	private String apiKey;

	@Value("${payos.checksum-key}")
	private String checkSumKey;

	@Override
	public String generateCassoQr(PaymentEntity payment) {
		return "https://img.vietqr.io/image/OCB-0949892138-compact.png" + "?amount=" + payment.getAmount() + "&addInfo="
				+ payment.getContent() + "&accountName=VJSHOW";
	}

	@Override
	public String generatePayOsQR(PaymentEntity payment) {
		
	    final PayOS payOS = new PayOS(clientId, apiKey, checkSumKey);
	    
	    Long orderCode = System.currentTimeMillis() / 1000;
        CreatePaymentLinkRequest paymentData =
            CreatePaymentLinkRequest.builder()
                .orderCode(orderCode)
                .amount(payment.getAmount())
                .description(payment.getContent())
                .returnUrl("https://vjshow.vn/payment-result")
                .cancelUrl("https://vjshow.vn/payment-result")
                .build();
        
        CreatePaymentLinkResponse result = payOS.paymentRequests().create(paymentData);
        return result.getQrCode();
	    

//		WebClient webClient = WebClient.builder().baseUrl("https://api-merchant.payos.vn")
//				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
//
//		Map<String, Object> body = new HashMap<>();
//		body.put("orderCode", payment.getId());
//		body.put("amount", payment.getAmount());
//		body.put("description", payment.getContent());
//		body.put("returnUrl", "https://vjshow.vn/success-create-qr");
//		body.put("cancelUrl", "https://vjshow.vn/error-create-qr");
//
//		PayOSResponse response = webClient.post().uri("/v2/payment-requests").header("x-client-id", clientId)
//				.header("x-api-key", apiKey).bodyValue(body).retrieve().bodyToMono(PayOSResponse.class).block();
//
//		// parse response
//		String url = response.getData().getCheckoutUrl();

		return url;
	}
}
