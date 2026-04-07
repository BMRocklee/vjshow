package com.vjshow.marketplace.entity;

import java.time.LocalDateTime;

import com.vjshow.marketplace.enums.PaymentStatusEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "payments")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEntity {
	
	@Id
	private String id; // UUID

	private Long amount;

	private String content; // nội dung chuyển khoản (VJSHOW-xxx)

    @Enumerated(EnumType.STRING)
	private PaymentStatusEnum status; // PENDING | PAID | EXPIRED

	private LocalDateTime createdAt;

	private LocalDateTime paidAt;
	
	
}
