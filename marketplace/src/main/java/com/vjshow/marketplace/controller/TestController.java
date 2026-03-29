package com.vjshow.marketplace.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vjshow.marketplace.service.CloudFlareService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TestController {
	
	private final CloudFlareService r2Service;
	
	@PostMapping("/upload-test")
	public String upload(@RequestParam MultipartFile file) {
	    String key = "test/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
	    return r2Service.upload(file, key);
	}
	
	@GetMapping("/data-deletion")
    public Map<String, String> delete() {
        return Map.of(
            "url", "https://vjshow.vn",
            "confirmation_code", "123456"
        );
    }
}
