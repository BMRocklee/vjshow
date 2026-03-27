package com.vjshow.marketplace.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WorkerService {

	private final WebClient webClient;

	public WorkerService(@Value("${worker.url}") String workerUrl) {
		this.webClient = WebClient.builder().baseUrl(workerUrl).build();
	}

	public void sendJob(String key, String type) {
		webClient.post().uri("/queue/job").bodyValue(Map.of("key", key, "type", type)).retrieve()
				.bodyToMono(String.class).subscribe();
	}
}
