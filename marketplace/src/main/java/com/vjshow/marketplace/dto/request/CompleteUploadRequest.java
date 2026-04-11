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

	private String fileKey; 

	private Long fileSize;
	
	// ===== PREVIEW =====
    private String previewKey;   // ảnh preview (nullable)

    // ===== META =====
	private String name;
	private String type;
    private String imageMode;    // NORMAL / SELL_FILE (nullable)
    private String description;
    private Long price;

	private String contentType;
	private Long duration;
}
