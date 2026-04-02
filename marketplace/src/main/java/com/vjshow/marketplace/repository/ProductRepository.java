package com.vjshow.marketplace.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vjshow.marketplace.entity.ProductEntity;
import com.vjshow.marketplace.enums.ProductStatusEnum;
import com.vjshow.marketplace.enums.ProductTypeEnum;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

	ProductEntity findByFileKey(String fileKey);

	List<ProductEntity> findByNameContainingIgnoreCase(String keyword);

	List<ProductEntity> findByTypeAndNameContainingIgnoreCase(ProductTypeEnum valueOf, String keyword);

	Page<ProductEntity> findByStatusAndNameContainingIgnoreCase(ProductStatusEnum status, String name,
			Pageable pageable);

	Page<ProductEntity> findByStatusAndTypeAndNameContainingIgnoreCase(ProductStatusEnum status, ProductTypeEnum type,
			String name, Pageable pageable);

	List<ProductEntity> findByCreatorIdOrderByCreatedAtDesc(Long creatorId);

	List<ProductEntity> findTop5ByTypeOrderByTotalSalesDesc(ProductTypeEnum type);

	List<ProductEntity> findByTypeOrderByTotalSalesDesc(ProductTypeEnum type, Pageable pageable);

}
