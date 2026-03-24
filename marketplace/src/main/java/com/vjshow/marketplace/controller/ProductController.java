package com.vjshow.marketplace.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vjshow.marketplace.dto.request.HandleFileDoneRequest;
import com.vjshow.marketplace.entity.ProductEntity;
import com.vjshow.marketplace.enums.ProductStatusEnum;
import com.vjshow.marketplace.enums.ProductTypeEnum;
import com.vjshow.marketplace.exception.LogicException;
import com.vjshow.marketplace.repository.ProductRepository;
import com.vjshow.marketplace.service.WorkerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

	private final WorkerService workerService;

	private final ProductRepository productRepository;
	
	@GetMapping
	public List<ProductEntity> getProducts(@RequestParam(required = false) String type,
			@RequestParam(required = false, defaultValue = "") String keyword) {
		if (type == null || type.isEmpty()) {
			return productRepository.findByNameContainingIgnoreCase(keyword);
		}

		return productRepository.findByTypeAndNameContainingIgnoreCase(ProductTypeEnum.valueOf(type), keyword);
	}
	
	@GetMapping("/{id}")
	public ProductEntity getById(@PathVariable Long id) {
	    return productRepository.findById(id)
	        .orElseThrow(() -> new LogicException("NOT_FOUND","Không tìm thấy sản phẩm"));
	}

	@PostMapping("/upload")
	public ProductEntity upload(@RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "price", required = false) Long price) throws Exception {

		String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

		Path path = Paths.get("uploads/" + fileName);
		Files.createDirectories(path.getParent());
		Files.write(path, file.getBytes());

		ProductEntity product = ProductEntity.builder().name(name).description(description)
				.type(ProductTypeEnum.valueOf(type)).fileKey(path.toString()).price(price)
				.status(ProductStatusEnum.UPLOADED).build();

		product = productRepository.save(product);

		if (product.getType() == ProductTypeEnum.IMAGE) {
			// gọi worker
			workerService.sendImageJob(path.toString());
		} else if (product.getType() == ProductTypeEnum.VIDEO) {
			workerService.sendVideoJob(path.toString());
		}

		return product;
	}

	@PostMapping("/done")
	public void done(@RequestBody HandleFileDoneRequest fileInfo) {
		String fileKey = fileInfo.getFileKey();

		ProductEntity product = productRepository.findByFileKey(fileKey);

		String preview = fileInfo.getPreviewUrl();
		preview = preview.replace("uploads/", "").replace("uploads\\", "").replace("\\", "/");
		product.setPreviewUrl("http://localhost:8080/files/" + preview);
		product.setWidth(fileInfo.getWidth());
		product.setHeight(fileInfo.getHeight());
		product.setDuration(fileInfo.getDuration());
		product.setFormat(fileInfo.getFormat());
		product.setSize(fileInfo.getSize());
		
		product.setStatus(ProductStatusEnum.DONE);
		productRepository.save(product);
	}
}
