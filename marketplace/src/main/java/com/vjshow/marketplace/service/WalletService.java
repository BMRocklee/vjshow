package com.vjshow.marketplace.service;

import java.util.List;

import com.vjshow.marketplace.dto.response.TransactionResponse;
import com.vjshow.marketplace.dto.response.WalletResponse;
import com.vjshow.marketplace.dto.response.WithdrawResponse;
import com.vjshow.marketplace.entity.OrderEntity;

public interface WalletService {
	public void handleOrderPaid(OrderEntity order);

	public void createWithdraw(Long creatorId, Long amount, String bankInfo);

	public void approveWithdraw(String withdrawId);
	
	public WalletResponse getWallet(Long creatorId);

	public List<TransactionResponse> getTransactions(Long creatorId);

	public List<WithdrawResponse> getWithdraws(Long creatorId);
}
