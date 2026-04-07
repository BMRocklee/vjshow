package com.vjshow.marketplace.config;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vjshow.marketplace.entity.CreatorWalletEntity;
import com.vjshow.marketplace.repository.CreatorWalletRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WalletScheduler {
    
    private final CreatorWalletRepository walletRepo;

    @Scheduled(cron = "0 */5 * * * *")
    @Transactional
    public void releasePending() {

        List<CreatorWalletEntity> wallets = walletRepo.findAll();

        for (CreatorWalletEntity wallet : wallets) {
            if (wallet.getPendingBalance() > 0) {

                wallet.releasePending(wallet.getPendingBalance());
            }
        }
    }
}
