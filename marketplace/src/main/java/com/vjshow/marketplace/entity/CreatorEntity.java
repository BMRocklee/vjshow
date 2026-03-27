package com.vjshow.marketplace.entity;

import java.time.LocalDateTime;

import com.vjshow.marketplace.enums.CreatorStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Getter
@Setter
@Table(name = "creators")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatorEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;

	private String name;

	private String email;

	private String phone;

	private String type;

	private String company;

	private Long uploadLimit;

	private Long usedStorage;

	@Enumerated(EnumType.STRING)
	private CreatorStatus status;

	private LocalDateTime createdAt;

	private LocalDateTime reviewedAt;

	@PrePersist
	public void prePersist() {
		if (this.uploadLimit == null) {
			this.uploadLimit = 5 * 1024 * 1024 * 1024L;
		}
		
		if (this.usedStorage == null) {
			this.usedStorage = 0L;
		}
	}
}
