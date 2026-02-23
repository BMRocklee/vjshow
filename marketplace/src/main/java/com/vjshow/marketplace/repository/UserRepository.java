package com.vjshow.marketplace.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vjshow.marketplace.entity.User;


public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByPublicId(UUID publicId);
	
	Optional<User> findByEmail(String email);
}
