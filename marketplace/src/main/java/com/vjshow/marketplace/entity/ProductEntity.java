package com.vjshow.marketplace.entity;

import java.time.LocalDateTime;

import com.vjshow.marketplace.enums.ProductTypeEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// creator sở hữu
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creator_id")
	private CreatorEntity creator;

	private String name;

	@Column(columnDefinition = "TEXT")
	private String description;

	// giá bán
	private Long price;

	// loại file: IMAGE / VIDEO / FILE
    @Enumerated(EnumType.STRING)
	private ProductTypeEnum type;

	// thumbnail preview
	private String thumbnailUrl;

	// preview public (ảnh demo / video preview)
	private String previewUrl;

	// key trên cloudflare R2 (private)
	private String fileKey;

	// url tạm (pre-signed)
	private String presignedUrl;

	private Long totalSales;

	private Boolean isActive;

	private LocalDateTime createdAt;
}
