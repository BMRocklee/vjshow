package com.vjshow.marketplace.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompleteUploadRequest {

	private String key;

	private Long fileSize;

	private String name;

	private String type;

	private String description;

	private Long price;

	// 🔥 optional (nên có)
	private String contentType;
	
	private Long duration;
}