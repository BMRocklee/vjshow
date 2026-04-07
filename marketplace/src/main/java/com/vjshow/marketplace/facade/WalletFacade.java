package com.vjshow.marketplace.facade;

import com.vjshow.marketplace.entity.OrderEntity;

public interface WalletFacade {
	public void onOrderPaid(OrderEntity order);
	
	public void requestWithdraw(Long creatorId, Long amount, String bankInfo);

	public void adminApproveWithdraw(String withdrawId);
}
