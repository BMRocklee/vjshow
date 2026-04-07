package com.vjshow.marketplace.facade;

import java.util.List;

import com.vjshow.marketplace.dto.response.TransactionResponse;
import com.vjshow.marketplace.dto.response.WalletResponse;
import com.vjshow.marketplace.dto.response.WithdrawResponse;
import com.vjshow.marketplace.entity.OrderEntity;

public interface WalletFacade {
	public void onOrderPaid(OrderEntity order);

	public void requestWithdraw(Long creatorId, Long amount, String bankInfo);

	public void adminApproveWithdraw(String withdrawId);

	public WalletResponse getMyWallet(Long creatorId);

	public List<TransactionResponse> getMyTransactions(Long creatorId);

	public List<WithdrawResponse> getMyWithdraws(Long creatorId);
}
