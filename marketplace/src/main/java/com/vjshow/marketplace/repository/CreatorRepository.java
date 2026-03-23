package com.vjshow.marketplace.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vjshow.marketplace.entity.CreatorEntity;
import com.vjshow.marketplace.entity.UserEntity;
import com.vjshow.marketplace.enums.CreatorStatus;



public interface CreatorRepository  extends JpaRepository<CreatorEntity, Long> {
	
    boolean existsByUserAndStatus(UserEntity user, CreatorStatus status);
        
    List<CreatorEntity> findAllByOrderByCreatedAtDesc();

    Optional<CreatorEntity> findTopByUserOrderByCreatedAtDesc(UserEntity user);
    
    List<CreatorEntity> findByStatus(CreatorStatus status);

    long countByStatus(CreatorStatus status);
}
