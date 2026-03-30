package com.vjshow.marketplace.dto.response;

import java.time.LocalDateTime;

import com.vjshow.marketplace.enums.ProductTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProductResponse {

    private Long id;
    private String name;
    private Long price;

    private String status;
    private ProductTypeEnum type;

    private String thumbnailUrl;
    private String previewUrl;

    private Boolean isActive;

    private LocalDateTime createdAt;

    // 🔥 thêm thông số media
    private Long width;
    private Long height;
    private Long duration; // video/audio
    private Long size;     // bytes
    private String format; // mp4, jpg...

    // 🔥 business
    private Long totalSales;

    // 🔥 tiện cho UI
    private String resolution; // "1920x1080"
    private String sizeText;   // "2.3 MB"
}
