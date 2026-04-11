package com.vjshow.marketplace.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vjshow.marketplace.dto.request.CompleteUploadRequest;
import com.vjshow.marketplace.dto.request.ProductRequest;
import com.vjshow.marketplace.dto.response.ProductPageResponse;
import com.vjshow.marketplace.dto.response.UserProductResponse;
import com.vjshow.marketplace.entity.CreatorEntity;
import com.vjshow.marketplace.entity.ProductEntity;
import com.vjshow.marketplace.enums.ProductStatusEnum;
import com.vjshow.marketplace.enums.ProductTypeEnum;
import com.vjshow.marketplace.exception.LogicException;
import com.vjshow.marketplace.mapper.ProductMapper;
import com.vjshow.marketplace.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;

	private final CloudFlareService r2Service;

	private final ProductMapper productMapper;
	
	private final CreatorService creatorService;

	public ProductEntity getById(Long id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new LogicException("NOT_FOUND", "không tồn tại sản phẩm"));
	}

	@Override
	public ProductEntity createProduct(CompleteUploadRequest request, CreatorEntity creator) {
//		ProductEntity product = ProductEntity.builder()
//				.name(request.getName())
//				.description(request.getDescription())
//				.type(ProductTypeEnum.valueOf(request.getType()))
//				.fileKey(request.getKey())
//				.price(request.getPrice())
//				.status(ProductStatusEnum.PROCESSING)
//				.duration(request.getDuration())
//				.creator(creator)
//				.format(request.getContentType())
//				.size(request.getFileSize())
//				.build();
		
		
		ProductTypeEnum type = ProductTypeEnum.valueOf(request.getType());
	    ProductEntity product = new ProductEntity();

	    product.setName(request.getName());
	    product.setDescription(request.getDescription());
	    product.setType(type);
	    product.setPrice(request.getPrice());
	    product.setCreator(creator);
	    product.setFormat(request.getContentType());
	    product.setDuration(request.getDuration());
	    product.setSize(request.getFileSize() != null ? request.getFileSize() : 0L);
	    product.setStatus(ProductStatusEnum.PROCESSING);
	    product.setFileKey(request.getFileKey());
	    product.setPreviewUrl(request.getPreviewKey());

		return productRepository.save(product);
	}

	@Override
	public List<ProductEntity> getByCreatorId(Long creatorId) {
		return productRepository.findByCreatorIdAndDeletedFlagFalseOrderByCreatedAtDesc(creatorId);
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
	@Transactional
	public void delete(Long id) {
		ProductEntity product = productRepository.findById(id)
				.orElseThrow(() -> new LogicException("NOT_FOUND", "Không tìm thấy sản phẩm"));

		// ❌ Check nếu đã có order
		//		boolean exists = orderRepository.existsByProduct_Id(id);
		//		if (exists) {
		//			throw new LogicException("INVALID", "Sản phẩm đã có người mua, không thể xóa");
		//		}

		// ✅ Xóa file trên R2
		deleteFileIfExists(product.getPreviewUrl());
		deleteFileIfExists(product.getFileKey());
		deleteFileIfExists(product.getThumbnailUrl());
		deleteHlsByUrl(product.getHlsVideoUrl());
		
		// giảm storage
		creatorService.decreaseStorage(product.getCreator(), product.getSize());

		productRepository.delete(product);
	}

	@Override
	public Page<ProductEntity> getPublicProducts(String type, String keyword, int page, int size) {

		if (keyword == null)
			keyword = "";

		ProductStatusEnum status = ProductStatusEnum.DONE;

		// ⚠️ Spring page bắt đầu từ 0
		Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());

		if (type == null || type.isEmpty()) {
			return productRepository.findByStatusAndNameContainingIgnoreCaseAndDeletedFlagFalse(status, keyword, pageable);
		}

		ProductTypeEnum productType = ProductTypeEnum.valueOf(type);

		return productRepository.findByStatusAndTypeAndNameContainingIgnoreCaseAndDeletedFlagFalse(status, productType, keyword, pageable);
	}

	@Override
	public List<ProductEntity> getTopProducts(ProductTypeEnum type) {
		return productRepository.findTop5ByTypeAndDeletedFlagFalseOrderByTotalSalesDesc(type);
	}

	@Override
	public Page<ProductEntity> getTopProducts(ProductTypeEnum type, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return productRepository.findByTypeAndStatusAndDeletedFlagFalseOrderByTotalSalesDesc(type,
				ProductStatusEnum.DONE, pageable);
	}

	private void deleteFileIfExists(String url) {
		try {
			r2Service.deleteFileByUrl(url);
		} catch (Exception e) {
			// log lại nhưng KHÔNG fail transaction
			log.error("Delete R2 file failed: {}", url, e);
		}
	}

	private void deleteHlsByUrl(String url) {
		try {
			r2Service.deleteHlsByKey(url);
		} catch (Exception e) {
			// log lại nhưng KHÔNG fail transaction
			log.error("Delete R2 file failed: {}", url, e);
		}
	}

	@Override
	public ProductPageResponse getByCreator(UUID publicId, ProductTypeEnum type, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);

		Page<ProductEntity> result = productRepository.findByCreator(publicId, type, pageable);

		List<UserProductResponse> items = result.getContent().stream().map(productMapper::toResponse).toList();

		return ProductPageResponse.builder().items(items).total(result.getTotalElements()).build();
	}
}
