package com.vjshow.marketplace.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WorkerService {
	 private final RestTemplate restTemplate = new RestTemplate();

	    public void sendVideoJob(String url) {
	        Map<String, String> body = Map.of("url", url);

	        restTemplate.postForObject(
	            "http://localhost:3001/queue/video",
	            body,
	            String.class
	        );
	    }

	    public void sendImageJob(String url) {
	        Map<String, String> body = Map.of("url", url);

	        restTemplate.postForObject(
	            "http://localhost:3001/queue/image",
	            body,
	            String.class
	        );
	    }
}
