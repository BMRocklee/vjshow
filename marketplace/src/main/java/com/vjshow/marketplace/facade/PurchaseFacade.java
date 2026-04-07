package com.vjshow.marketplace.facade;

import java.util.List;

import com.vjshow.marketplace.dto.response.PurchaseItemResponse;

public interface PurchaseFacade {
	List<PurchaseItemResponse> getMyPurchases(Long userId);

    String downloadProduct(Long userId, Long orderId);
}
