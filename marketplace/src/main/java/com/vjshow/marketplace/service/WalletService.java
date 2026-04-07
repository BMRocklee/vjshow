package com.vjshow.marketplace.service;

import com.vjshow.marketplace.entity.OrderEntity;

public interface WalletService {
	public void handleOrderPaid(OrderEntity order);

	public void createWithdraw(Long creatorId, Long amount, String bankInfo);

	public void approveWithdraw(String withdrawId);
}
