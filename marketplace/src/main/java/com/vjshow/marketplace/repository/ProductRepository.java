package com.vjshow.marketplace.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

	@Query("""
		    SELECT p FROM ProductEntity p
		    JOIN p.creator c
		    JOIN c.user u
		    WHERE u.publicId = :publicId
		      AND p.status = 'DONE'
		      AND (:type IS NULL OR p.type = :type)
		    ORDER BY p.createdAt DESC
		""")
	Page<ProductEntity> findByCreator(UUID publicId, ProductTypeEnum type, Pageable pageable);
	
	@Query("""
		    SELECT COALESCE(SUM(p.price * p.totalSales), 0)
		    FROM ProductEntity p
		    WHERE p.creator.id = :creatorId
		    AND p.status = 'ACTIVE'
		""")
		long sumRevenueByCreator(@Param("creatorId") Long creatorId);
}
