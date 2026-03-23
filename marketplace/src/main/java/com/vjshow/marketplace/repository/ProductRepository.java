package com.vjshow.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vjshow.marketplace.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

	ProductEntity findByFileKey(String fileKey);

}
