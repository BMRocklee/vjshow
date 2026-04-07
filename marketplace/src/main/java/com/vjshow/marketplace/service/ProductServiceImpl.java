package com.vjshow.marketplace.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vjshow.marketplace.dto.request.CompleteUploadRequest;
import com.vjshow.marketplace.dto.request.ProductRequest;
import com.vjshow.marketplace.entity.CreatorEntity;
import com.vjshow.marketplace.entity.ProductEntity;
import com.vjshow.marketplace.enums.ProductStatusEnum;
import com.vjshow.marketplace.enums.ProductTypeEnum;
import com.vjshow.marketplace.exception.LogicException;
import com.vjshow.marketplace.repository.OrderRepository;
import com.vjshow.marketplace.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	
	private final OrderRepository orderRepository;
	
	private final CloudFlareService r2Service;

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
	@Transactional
	public void delete(Long id) {
		ProductEntity product = productRepository.findById(id)
				.orElseThrow(() -> new LogicException("NOT_FOUND", "Không tìm thấy sản phẩm"));
		
		 // ❌ Check nếu đã có order
	    boolean exists = orderRepository.existsByProduct_Id(id);
	    if (exists) {
	        throw new LogicException("INVALID", "Sản phẩm đã có người mua, không thể xóa");
	    }
	    
	    // ✅ Xóa file trên R2
	    deleteFileIfExists(product.getPreviewUrl());
	    deleteFileIfExists(product.getThumbnailUrl());
	    deleteHlsByUrl(product.getHlsVideoUrl());
		
		productRepository.delete(product);
	}
 
	@Override
	public Page<ProductEntity> getPublicProducts(String type, String keyword, int page, int size) {

	    if (keyword == null) keyword = "";

	    ProductStatusEnum status = ProductStatusEnum.DONE;

	    // ⚠️ Spring page bắt đầu từ 0
	    Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());

	    if (type == null || type.isEmpty()) {
	        return productRepository
	                .findByStatusAndNameContainingIgnoreCase(status, keyword, pageable);
	    }

	    ProductTypeEnum productType = ProductTypeEnum.valueOf(type);

	    return productRepository
	            .findByStatusAndTypeAndNameContainingIgnoreCase(status, productType, keyword, pageable);
	}

	@Override
	public List<ProductEntity> getTopProducts(ProductTypeEnum type) {
		return productRepository.findTop5ByTypeOrderByTotalSalesDesc(type);
	}
	
	@Override
	public List<ProductEntity> getTopProducts(ProductTypeEnum type, Long quantity) {
		Pageable pageable = PageRequest.of(0, quantity.intValue());
		return productRepository.findByTypeOrderByTotalSalesDesc(type, pageable);
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
}
