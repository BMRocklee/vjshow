package com.vjshow.marketplace.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.vjshow.marketplace.enums.AuthProviderEnum;
import com.vjshow.marketplace.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users", indexes = { 
		@Index(name = "idx_user_public_id", columnList = "public_id"),
		@Index(name = "idx_user_email", columnList = "email") 
		},
	uniqueConstraints = {
			@UniqueConstraint(columnNames =  {"provider", "provider_id"})
		})
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
	@Column(name = "public_id", nullable = false, unique = true, updatable = false)
	@UuidGenerator(style = UuidGenerator.Style.TIME)
	private UUID publicId;

	// Email (login)
	@Column(nullable = false)
	private String email;
	
	// Name
    @Column(nullable = false)
    private String name;
    
    @Enumerated(EnumType.STRING)
    private AuthProviderEnum provider;
    
    @Column(name = "provider_id")
    private String providerId;
    
    private String picture;

    private String password;
    
    @Enumerated(EnumType.STRING)
    private Role role;
    
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    
    @PrePersist
    public void prePersist() {

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (role == null)
            role = Role.USER;
    }
    
    @PreUpdate
    public void preUpdate() {

        updatedAt = LocalDateTime.now();

    }

}
