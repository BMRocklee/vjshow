package com.vjshow.marketplace.facade;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.vjshow.marketplace.dto.request.HandleFileDoneRequest;
import com.vjshow.marketplace.dto.response.UserProductResponse;
import com.vjshow.marketplace.entity.ProductEntity;
import com.vjshow.marketplace.enums.ProductStatusEnum;
import com.vjshow.marketplace.enums.ProductTypeEnum;
import com.vjshow.marketplace.exception.LogicException;
import com.vjshow.marketplace.mapper.ProductMapper;
import com.vjshow.marketplace.repository.ProductRepository;
import com.vjshow.marketplace.service.ProductService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductFacadeImpl implements ProductFacade {

	private final ProductService productService;

	private final ProductMapper productMapper;

	private final ProductRepository productRepository;

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
	public void markDone(HandleFileDoneRequest fileInfo) {
		ProductEntity product = productRepository.findByFileKey(fileInfo.getFileKey());

		if (product == null) {
			throw new LogicException("NOT_FOUND", "Không tìm thấy sản phẩm với fileKey");
		}

		product.setPreviewUrl(fileInfo.getPreviewUrl());
		product.setThumbnailUrl(fileInfo.getThumbUrl());
		product.setHlsVideoUrl(fileInfo.getHlsUrl());
		product.setWidth(fileInfo.getWidth());
		product.setHeight(fileInfo.getHeight());
		product.setDuration(fileInfo.getDuration());
		product.setStatus(ProductStatusEnum.DONE);
		productRepository.save(product);
	}

	@Override
	public List<UserProductResponse> getTopProducts(ProductTypeEnum type, Long quantity) {
		List<ProductEntity> productList = productService.getTopProducts(type, quantity);

		List<UserProductResponse> response = productList.stream().map(productMapper::toResponse).toList();

		return response;
	}
}
