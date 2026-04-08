package com.vjshow.marketplace.service;

import com.vjshow.marketplace.dto.request.CassoWebhookDto;
import com.vjshow.marketplace.dto.response.PayOSWebhookDto;

public interface WebhookService {
	void handleCasso(CassoWebhookDto body);
	
	void handlePayOS(PayOSWebhookDto body);
}
