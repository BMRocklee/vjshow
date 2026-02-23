package com.vjshow.marketplace.entity;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users", indexes = { @Index(name = "idx_user_public_id", columnList = "public_id"),
		@Index(name = "idx_user_email", columnList = "email") })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

	// Internal ID (primary key)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Public ID (UUID)
	@Column(name = "public_id", nullable = false, unique = true, updatable = false, columnDefinition = "BINARY(16)")
	@UuidGenerator(style = UuidGenerator.Style.TIME)
	private UUID publicId;

	// Email (login)
	@Column(nullable = false, unique = true, length = 100)
	private String email;
	
	// Name
    @Column(nullable = false, length = 100)
    private String name;

    // Password
    @Column(nullable = false)
    private String password;

}
