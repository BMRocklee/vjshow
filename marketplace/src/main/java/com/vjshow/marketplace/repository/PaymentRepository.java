package com.vjshow.marketplace.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vjshow.marketplace.entity.PaymentEntity;

public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {	
	
	@Query("SELECT p FROM PaymentEntity p WHERE :content LIKE CONCAT(p.content, '%')")
	Optional<PaymentEntity> findByContentInText(@Param("content") String content);
}
