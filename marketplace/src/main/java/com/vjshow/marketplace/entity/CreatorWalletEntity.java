package com.vjshow.marketplace.entity;

import java.time.LocalDateTime;

import com.vjshow.marketplace.exception.LogicException;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "creator_wallet")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatorWalletEntity {

	@Id
	private Long creatorId;

	private Long availableBalance; // có thể rút

	private Long pendingBalance; // chờ

	private Long totalEarned;

	private LocalDateTime updatedAt;

	public void addPending(Long amount) {
		this.pendingBalance += amount;
		this.totalEarned += amount;
	}

	public void releasePending(Long amount) {
		this.pendingBalance -= amount;
		this.availableBalance += amount;
	}

	public void subtractAvailable(Long amount) {
		if (this.availableBalance < amount) {
			throw new LogicException("INSUFFICIENT_BALANCE", "số tiền rút không thể nhiều hơn số tiền sẵn có");
		}
		this.availableBalance -= amount;
	}
}
