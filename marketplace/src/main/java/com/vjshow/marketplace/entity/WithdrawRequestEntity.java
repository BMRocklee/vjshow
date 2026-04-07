package com.vjshow.marketplace.entity;

import java.time.LocalDateTime;

import com.vjshow.marketplace.enums.WithdrawStatusEnum;

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
@Table(name = "withdraw_requests")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawRequestEntity {

	@Id
	private String id;

	private Long creatorId;

	private Long amount;

	private String bankInfo;

	@Enumerated(EnumType.STRING)
	private WithdrawStatusEnum status;
	// PENDING / APPROVED / REJECTED / PAID

	private LocalDateTime createdAt;

	private LocalDateTime processedAt;

	@PrePersist
	public void prePersist() {
		createdAt = LocalDateTime.now();
		status = WithdrawStatusEnum.PENDING;
	}
}
