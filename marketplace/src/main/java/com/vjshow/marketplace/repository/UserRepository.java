package com.vjshow.marketplace.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vjshow.marketplace.entity.User;
import com.vjshow.marketplace.enums.AuthProviderEnum;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByPublicId(UUID publicId);

	@Query("SELECT u FROM User u WHERE u.email = :email")
	Optional<User> findByEmail(String email);

	Optional<User> findByProviderAndProviderId(AuthProviderEnum provider, String providerId);
}
