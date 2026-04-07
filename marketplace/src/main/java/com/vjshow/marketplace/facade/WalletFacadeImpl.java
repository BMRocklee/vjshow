package com.vjshow.marketplace.facade;

import java.util.List;

import org.springframework.stereotype.Component;

import com.vjshow.marketplace.dto.response.TransactionResponse;
import com.vjshow.marketplace.dto.response.WalletResponse;
import com.vjshow.marketplace.dto.response.WithdrawResponse;
import com.vjshow.marketplace.entity.OrderEntity;
import com.vjshow.marketplace.service.WalletService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WalletFacadeImpl implements WalletFacade {

    private final WalletService walletService;

    @Override
    public void onOrderPaid(OrderEntity order) {
        walletService.handleOrderPaid(order);
    }

    @Override
    public void requestWithdraw(Long creatorId, Long amount, String bankInfo) {
        walletService.createWithdraw(creatorId, amount, bankInfo);
    }

    @Override
    public void adminApproveWithdraw(String withdrawId) {
        walletService.approveWithdraw(withdrawId);
    }

	@Override
	public WalletResponse getMyWallet(Long creatorId) {
		return walletService.getWallet(creatorId);
	}

	@Override
	public List<TransactionResponse> getMyTransactions(Long creatorId) {
		return walletService.getTransactions(creatorId);
	}

	@Override
	public List<WithdrawResponse> getMyWithdraws(Long creatorId) {
		 return walletService.getWithdraws(creatorId);
	}
}
