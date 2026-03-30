package com.vjshow.marketplace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vjshow.marketplace.entity.ProductEntity;
import com.vjshow.marketplace.enums.ProductTypeEnum;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

	ProductEntity findByFileKey(String fileKey);

	List<ProductEntity> findByNameContainingIgnoreCase(String keyword);

	List<ProductEntity> findByTypeAndNameContainingIgnoreCase(ProductTypeEnum valueOf, String keyword);
	
	List<ProductEntity> findByCreatorIdOrderByCreatedAtDesc(Long creatorId);

}
