package com.vjshow.marketplace.facade;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.vjshow.marketplace.dto.request.HandleFileDoneRequest;
import com.vjshow.marketplace.dto.response.PageResponse;
import com.vjshow.marketplace.dto.response.UserProductResponse;
import com.vjshow.marketplace.entity.ProductEntity;
import com.vjshow.marketplace.enums.ProductStatusEnum;
import com.vjshow.marketplace.enums.ProductTypeEnum;
import com.vjshow.marketplace.exception.LogicException;
import com.vjshow.marketplace.mapper.ProductMapper;
import com.vjshow.marketplace.repository.ProductRepository;
import com.vjshow.marketplace.service.CloudFlareService;
import com.vjshow.marketplace.service.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductFacadeImpl implements ProductFacade {

	private final ProductService productService;

	private final ProductMapper productMapper;

	private final ProductRepository productRepository;

	private final CloudFlareService r2Service;

	@Override
	public Page<UserProductResponse> getPublicProducts(String type, String keyword, int page, int size) {

		Page<ProductEntity> data = productService.getPublicProducts(type, keyword, page, size);

		return data.map(productMapper::toResponse);
	}

	@Override
	public UserProductResponse getById(Long id) {
		ProductEntity p = productRepository.findById(id)
				.orElseThrow(() -> new LogicException("NOT_FOUND", "Không tìm thấy sản phẩm"));

		// chỉ cho phép xem product DONE
		if (p.getStatus() != ProductStatusEnum.DONE) {
			throw new LogicException("NOT_PUBLIC", "Sản phẩm đang xử lý");
		}

		return productMapper.toResponse(p);
	}

	@Override
	public boolean markDone(HandleFileDoneRequest fileInfo) {
		boolean statusUpload = false;
		ProductEntity product = productRepository.findByAnyKey(fileInfo.getFileKey());

		if (product == null) {
			throw new LogicException("NOT_FOUND", "Không tìm thấy sản phẩm");
		}

		// =====================
		// 🔥 CHECK DUPLICATE HASH
		// =====================
		Optional<ProductEntity> existed = Optional.empty();
		if (fileInfo.getHash() != null) {
			existed = productRepository.findByHashAndDeletedFlagFalse(fileInfo.getHash());
		}

		if (existed.isPresent() && !existed.get().getId().equals(product.getId())) {
			// delete file R2
			deleteFileIfExists(product.getFileKey());
			deleteFileIfExists(product.getPreviewUrl());
			deleteFileIfExists(product.getThumbnailUrl());
			deleteHlsByUrl(product.getHlsVideoUrl());
			productRepository.delete(product);
		} else {
			product.setHash(fileInfo.getHash());
			product.setPreviewUrl(fileInfo.getPreviewUrl());
			product.setThumbnailUrl(fileInfo.getThumbUrl());
			product.setHlsVideoUrl(fileInfo.getHlsUrl());
			product.setWidth(fileInfo.getWidth());
			product.setHeight(fileInfo.getHeight());
			product.setDuration(fileInfo.getDuration());
			product.setStatus(ProductStatusEnum.DONE);
			productRepository.save(product);
			statusUpload = true;
		}
		return statusUpload;
	}

	@Override
	public PageResponse<UserProductResponse> getTopProducts(ProductTypeEnum type, int page, int size) {
		Page<ProductEntity> result = productService.getTopProducts(type, page, size);

		List<UserProductResponse> data = result.getContent().stream().map(productMapper::toResponse).toList();

		return PageResponse.<UserProductResponse>builder().data(data).hasNext(result.hasNext())
				.totalElements(result.getTotalElements()).currentPage(page).build();
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
