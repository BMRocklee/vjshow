package com.vjshow.marketplace.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vjshow.marketplace.entity.ProductEntity;
import com.vjshow.marketplace.enums.ProductStatusEnum;
import com.vjshow.marketplace.enums.ProductTypeEnum;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

	ProductEntity findByFileKey(String fileKey);

	List<ProductEntity> findByNameContainingIgnoreCase(String keyword);

	List<ProductEntity> findByTypeAndNameContainingIgnoreCase(ProductTypeEnum valueOf, String keyword);

	List<ProductEntity> findByStatusAndNameContainingIgnoreCase(ProductStatusEnum status, String keyword);

	List<ProductEntity> findByStatusAndTypeAndNameContainingIgnoreCase(ProductStatusEnum status, ProductTypeEnum type,
			String keyword);

	List<ProductEntity> findByCreatorIdOrderByCreatedAtDesc(Long creatorId);

	List<ProductEntity> findTop5ByTypeOrderByTotalSalesDesc(ProductTypeEnum type);

	List<ProductEntity> findByTypeOrderByTotalSalesDesc(ProductTypeEnum type, Pageable pageable);

}
