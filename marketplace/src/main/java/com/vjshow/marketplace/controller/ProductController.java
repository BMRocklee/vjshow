package com.vjshow.marketplace.controller;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vjshow.marketplace.entity.ProductEntity;
import com.vjshow.marketplace.enums.ProductStatusEnum;
import com.vjshow.marketplace.enums.ProductTypeEnum;
import com.vjshow.marketplace.repository.ProductRepository;
import com.vjshow.marketplace.service.WorkerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
	
	private final WorkerService workerService;
	
	private final ProductRepository productRepository;

	@PostMapping("/upload")
	public ProductEntity upload(
	        @RequestParam("file") MultipartFile file,
	        @RequestParam("name") String name,
	        @RequestParam("type") String type,
	        @RequestParam("description") String description
	) throws Exception {

	    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

	    Path path = Paths.get("uploads/" + fileName);
	    Files.createDirectories(path.getParent());
	    Files.write(path, file.getBytes());

	    ProductEntity product = ProductEntity.builder()
	            .name(name)
	            .description(description)
	            .type(ProductTypeEnum.valueOf(type))
	            .fileKey(path.toString())
	            .status(ProductStatusEnum.UPLOADED)
	            .build();

	    product = productRepository.save(product);
	    
	    if (product.getType() == ProductTypeEnum.IMAGE) {
	    	// gọi worker
		    workerService.sendImageJob(path.toString());
		} else if(product.getType() == ProductTypeEnum.VIDEO) {
			workerService.sendVideoJob(path.toString());
		}

	    return product;
	}
	
	@PostMapping("/done")
	public void done(@RequestBody Map<String, String> body) {

	    String fileKey = body.get("fileKey");

	    ProductEntity product = productRepository.findByFileKey(fileKey);

	    product.setStatus(ProductStatusEnum.DONE);
	    product.setPreviewUrl(body.get("previewUrl"));

	    productRepository.save(product);
	}
}
