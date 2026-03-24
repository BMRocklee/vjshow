package com.vjshow.marketplace.service;

import com.vjshow.marketplace.dto.request.CassoWebhookDto;

public interface WebhookService {
	void handleCasso(CassoWebhookDto body);
}
