package com.vjshow.marketplace.entity;

import java.time.LocalDateTime;

import com.vjshow.marketplace.enums.TransactionTypeEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "wallet_transactions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletTransactionEntity {

	@Id
	private String id;

	private Long creatorId;

	private Long amount;

	@Enumerated(EnumType.STRING)
	private TransactionTypeEnum type;
	// SALE / WITHDRAW

	private String referenceId; // orderId hoặc withdrawId

	private LocalDateTime createdAt;

	@PrePersist
	public void prePersist() {
		createdAt = LocalDateTime.now();
	}
}