package com.vjshow.marketplace.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payment_methods")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	  // creator sở hữu
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private CreatorEntity creator;

	private String name;
	private String bankName;
	private String accountNumber;

	private Boolean isDefault;

	private LocalDateTime createdAt;

	@PrePersist
	public void prePersist() {

		createdAt = LocalDateTime.now();

		if (isDefault == null)
			isDefault = false;
	}
}
