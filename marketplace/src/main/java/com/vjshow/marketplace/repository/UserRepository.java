package com.vjshow.marketplace.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vjshow.marketplace.entity.UserEntity;
import com.vjshow.marketplace.enums.AuthProviderEnum;
import com.vjshow.marketplace.enums.Role;


public interface UserRepository extends JpaRepository<UserEntity, Long> {

	Optional<UserEntity> findByPublicId(UUID publicId);

	Optional<UserEntity> findByEmail(String email);

	Optional<UserEntity> findByProviderAndProviderId(AuthProviderEnum provider, String providerId);
	
	long countByRole(Role role);
}
