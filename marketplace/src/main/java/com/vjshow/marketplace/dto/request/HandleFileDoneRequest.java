package com.vjshow.marketplace.dto.request;

import lombok.Data;

@Data
public class HandleFileDoneRequest {
	private String fileKey;

    private String previewUrl;
    
    private String thumbUrl;
    
    private String hlsUrl;

    private Long width;

    private Long height;

    private Long duration;

    private String format;

    private Long size;
    
    private String hash;
}
