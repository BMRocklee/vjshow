package com.vjshow.marketplace.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vjshow.marketplace.dto.response.TransactionResponse;
import com.vjshow.marketplace.dto.response.WalletResponse;
import com.vjshow.marketplace.dto.response.WithdrawResponse;
import com.vjshow.marketplace.entity.CreatorWalletEntity;
import com.vjshow.marketplace.entity.OrderEntity;
import com.vjshow.marketplace.entity.WalletTransactionEntity;
import com.vjshow.marketplace.entity.WithdrawRequestEntity;
import com.vjshow.marketplace.enums.TransactionTypeEnum;
import com.vjshow.marketplace.enums.WithdrawStatusEnum;
import com.vjshow.marketplace.exception.LogicException;
import com.vjshow.marketplace.repository.CreatorWalletRepository;
import com.vjshow.marketplace.repository.WalletTransactionRepository;
import com.vjshow.marketplace.repository.WithdrawRequestRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class WalletServiceImpl implements WalletService {

    private final CreatorWalletRepository walletRepo;
    private final WalletTransactionRepository txRepo;
    private final WithdrawRequestRepository withdrawRepo;

    @Override
    public void handleOrderPaid(OrderEntity order) {
    	
    	 if (Boolean.TRUE.equals(order.getWalletProcessed())) {
    	        return; // tránh cộng tiền 2 lần
    	    }

        Long creatorId = order.getCreator().getId();
        Long net = order.getAmount() - order.getCommission();

        CreatorWalletEntity wallet = walletRepo.findById(creatorId)
                .orElseGet(() -> walletRepo.save(
                        CreatorWalletEntity.builder()
                                .creatorId(creatorId)
                                .availableBalance(0L)
                                .pendingBalance(0L)
                                .totalEarned(0L)
                                .build()
                ));

        wallet.addPending(net);
        
        order.setWalletProcessed(true); // 🔥 mark đã xử lý

        txRepo.save(WalletTransactionEntity.builder()
                .id(UUID.randomUUID().toString())
                .creatorId(creatorId)
                .amount(net)
                .type(TransactionTypeEnum.SALE)
                .referenceId(order.getId().toString())
                .build());
    }

    @Override
    public void createWithdraw(Long creatorId, Long amount, String bankInfo) {

        CreatorWalletEntity wallet = walletRepo.findById(creatorId)
                .orElseThrow();

        if (wallet.getAvailableBalance() < amount) {
            throw new LogicException("NOT_ENOUGH_BALANCE","số tiền bạn rút nhiều hơn số sẵn có");
        }

        withdrawRepo.save(WithdrawRequestEntity.builder()
                .id(UUID.randomUUID().toString())
                .creatorId(creatorId)
                .amount(amount)
                .bankInfo(bankInfo)
                .build());
    }

    @Override
    public void approveWithdraw(String withdrawId) {

        WithdrawRequestEntity req = withdrawRepo.findById(withdrawId)
                .orElseThrow();

        CreatorWalletEntity wallet = walletRepo.findById(req.getCreatorId())
                .orElseThrow();

        wallet.subtractAvailable(req.getAmount());

        req.setStatus(WithdrawStatusEnum.PAID);
        req.setProcessedAt(LocalDateTime.now());

        txRepo.save(WalletTransactionEntity.builder()
                .id(UUID.randomUUID().toString())
                .creatorId(req.getCreatorId())
                .amount(req.getAmount())
                .type(TransactionTypeEnum.WITHDRAW)
                .referenceId(req.getId())
                .build());
    }

	@Override
	@Transactional(readOnly = true)
	public WalletResponse getWallet(Long creatorId) {
		 CreatorWalletEntity wallet = walletRepo.findById(creatorId)
		            .orElseGet(() -> CreatorWalletEntity.builder()
		                    .creatorId(creatorId)
		                    .availableBalance(0L)
		                    .pendingBalance(0L)
		                    .totalEarned(0L)
		                    .build());

		    return WalletResponse.builder()
		            .availableBalance(wallet.getAvailableBalance())
		            .pendingBalance(wallet.getPendingBalance())
		            .totalEarned(wallet.getTotalEarned())
		            .build();
	}

	@Override
	public List<TransactionResponse> getTransactions(Long creatorId) {
		return txRepo.findByCreatorIdOrderByCreatedAtDesc(creatorId)
	            .stream()
	            .map(tx -> TransactionResponse.builder()
	                    .id(tx.getId())
	                    .amount(tx.getAmount())
	                    .type(tx.getType().name())
	                    .referenceId(tx.getReferenceId())
	                    .createdAt(tx.getCreatedAt())
	                    .build())
	            .toList();
	}

	@Override
	public List<WithdrawResponse> getWithdraws(Long creatorId) {
		return withdrawRepo.findByCreatorIdOrderByCreatedAtDesc(creatorId)
	            .stream()
	            .map(w -> WithdrawResponse.builder()
	                    .id(w.getId())
	                    .amount(w.getAmount())
	                    .status(w.getStatus().name())
	                    .bankInfo(w.getBankInfo())
	                    .createdAt(w.getCreatedAt())
	                    .processedAt(w.getProcessedAt())
	                    .build())
	            .toList();
	}
}