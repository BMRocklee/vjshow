package com.vjshow.marketplace.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HandleFileDoneRequest {
	private String fileKey;
    private String type;

    private String previewUrl;
    private String thumbnailUrl;

    private Long width;
    private Long height;
    private Long duration;

    private String format;
    private Long size;
}
