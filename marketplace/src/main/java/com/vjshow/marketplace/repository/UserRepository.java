package com.vjshow.marketplace.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vjshow.marketplace.entity.UserEntity;
import com.vjshow.marketplace.enums.AuthProviderEnum;
import com.vjshow.marketplace.enums.Role;
import com.vjshow.marketplace.enums.UserStatusEnum;


public interface UserRepository extends JpaRepository<UserEntity, Long> {

	Optional<UserEntity> findByPublicId(UUID publicId);

	Optional<UserEntity> findByEmail(String email);

	Optional<UserEntity> findByProviderAndProviderId(AuthProviderEnum provider, String providerId);
	
	long countByRole(Role role);

	List<UserEntity> findByStatusAndNameContainingIgnoreCase(UserStatusEnum status, String keyword);

	List<UserEntity> findByNameContainingIgnoreCase(String keyword);
	
	List<UserEntity> findByStatus(UserStatusEnum status);

	long countByStatus(UserStatusEnum status);
}
