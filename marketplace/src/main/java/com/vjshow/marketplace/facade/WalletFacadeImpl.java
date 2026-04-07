package com.vjshow.marketplace.facade;

import org.springframework.stereotype.Component;

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
}
