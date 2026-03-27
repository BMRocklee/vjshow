package com.vjshow.marketplace.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vjshow.marketplace.storage.CloudFlareService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TestController {
	
	private final CloudFlareService r2Service;
	
	@PostMapping("/upload-test")
	public String upload(@RequestParam MultipartFile file) {
	    String key = "test/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
	    return r2Service.upload(file, key);
	}
}
