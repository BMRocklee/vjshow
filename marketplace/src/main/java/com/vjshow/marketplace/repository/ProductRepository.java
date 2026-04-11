package com.vjshow.marketplace.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.vjshow.marketplace.entity.ProductEntity;
import com.vjshow.marketplace.enums.ProductStatusEnum;
import com.vjshow.marketplace.enums.ProductTypeEnum;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
	
	@Query("""
			    SELECT p FROM ProductEntity p
			    WHERE (p.fileKey = :key OR p.previewUrl = :key)
			      AND p.deletedFlag = false
			""")
	ProductEntity findByAnyKey(@Param("key") String key);

	List<ProductEntity> findByNameContainingIgnoreCaseAndDeletedFlagFalse(String keyword);

	List<ProductEntity> findByTypeAndNameContainingIgnoreCaseAndDeletedFlagFalse(ProductTypeEnum valueOf, String keyword);

	Page<ProductEntity> findByStatusAndNameContainingIgnoreCaseAndDeletedFlagFalse(ProductStatusEnum status, String name,
			Pageable pageable);

	Page<ProductEntity> findByStatusAndTypeAndNameContainingIgnoreCaseAndDeletedFlagFalse(ProductStatusEnum status, ProductTypeEnum type,
			String name, Pageable pageable);

	List<ProductEntity> findByCreatorIdAndDeletedFlagFalseOrderByCreatedAtDesc(Long creatorId);

	List<ProductEntity> findTop5ByTypeAndDeletedFlagFalseOrderByTotalSalesDesc(ProductTypeEnum type);
	
	Optional<ProductEntity> findByHashAndDeletedFlagFalse(String hash);

	Page<ProductEntity> findByTypeAndStatusAndDeletedFlagFalseOrderByTotalSalesDesc(
		    ProductTypeEnum type,
		    ProductStatusEnum status,
		    Pageable pageable
		);

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
		    AND p.deletedFlag = false
		""")
		long sumRevenueByCreator(@Param("creatorId") Long creatorId);
	
	@Transactional
	@Modifying
	@Query("""
	    UPDATE ProductEntity p 
	    SET p.totalSales = COALESCE(p.totalSales, 0) + 1 
	    WHERE p.id = :id
	""")
	void incrementSales(@Param("id") Long id);
}
