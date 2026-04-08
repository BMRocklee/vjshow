package com.vjshow.marketplace.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vjshow.marketplace.dto.request.CassoWebhookDto;
import com.vjshow.marketplace.dto.response.PayOSWebhookDto;
import com.vjshow.marketplace.service.WebhookService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/webhook")
@RequiredArgsConstructor
public class PaymentWebhookController {
	
	private final WebhookService webhookService;
	
	@PostMapping("/casso")
	public ResponseEntity<?> handleCasso(@RequestBody CassoWebhookDto cassoDto) {
		webhookService.handleCasso(cassoDto);
		return ResponseEntity.ok().build();
	}
	
	
	@PostMapping("/payos")
    public ResponseEntity<?> handlePayOS(@RequestBody PayOSWebhookDto body) {
		webhookService.handlePayOS(body);
		return ResponseEntity.ok().build();
    }
	
}
