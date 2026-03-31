package com.vjshow.marketplace.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.vjshow.marketplace.dto.request.CompleteUploadRequest;
import com.vjshow.marketplace.dto.request.ProductRequest;
import com.vjshow.marketplace.entity.CreatorEntity;
import com.vjshow.marketplace.entity.ProductEntity;
import com.vjshow.marketplace.enums.ProductStatusEnum;
import com.vjshow.marketplace.enums.ProductTypeEnum;
import com.vjshow.marketplace.exception.LogicException;
import com.vjshow.marketplace.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;

	public ProductEntity getById(Long id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new LogicException("NOT_FOUND", "không tồn tại sản phẩm"));
	}

	@Override
	public ProductEntity createProduct(CompleteUploadRequest request, CreatorEntity creator) {
		ProductEntity product = ProductEntity.builder().name(request.getName()).description(request.getDescription())
				.type(ProductTypeEnum.valueOf(request.getType())).fileKey(request.getKey()).price(request.getPrice())
				.status(ProductStatusEnum.UPLOADED).duration(request.getDuration()).creator(creator)
				.format(request.getContentType()).size(request.getFileSize()).build();

		return productRepository.save(product);
	}

	@Override
	public List<ProductEntity> getByCreatorId(Long creatorId) {
		return productRepository.findByCreatorIdOrderByCreatedAtDesc(creatorId);
	}

	@Override
	public ProductEntity update(Long id, ProductRequest request) {
		ProductEntity product = productRepository.findById(id)
				.orElseThrow(() -> new LogicException("NOT_FOUND", "Không tìm thấy sản phẩm"));

		product.setName(request.getName());
		product.setPrice(request.getPrice());

		return productRepository.save(product);
	}

	@Override
	public void delete(Long id) {
		ProductEntity product = productRepository.findById(id)
				.orElseThrow(() -> new LogicException("NOT_FOUND", "Không tìm thấy sản phẩm"));
		productRepository.delete(product);
	}

	@Override
	public List<ProductEntity> getPublicProducts(String type, String keyword) {
		// normalize keyword
		if (keyword == null)
			keyword = "";

		// only DONE products
		ProductStatusEnum status = ProductStatusEnum.DONE;

		if (type == null || type.isEmpty()) {
			return productRepository.findByStatusAndNameContainingIgnoreCase(status, keyword);
		}

		ProductTypeEnum productType = ProductTypeEnum.valueOf(type);

		return productRepository.findByStatusAndTypeAndNameContainingIgnoreCase(status, productType, keyword);
	}
}
