package com.vjshow.marketplace.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLDelete;

import com.vjshow.marketplace.enums.FileFormatEnum;
import com.vjshow.marketplace.enums.ProductStatusEnum;
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
import jakarta.persistence.PrePersist;
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
@SQLDelete(sql = "UPDATE products SET deleted_flag = true WHERE id = ?")
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
    
    @Enumerated(EnumType.STRING)
    private ProductStatusEnum status;

	// thumbnail preview
	private String thumbnailUrl;

	// preview sau khi worker xử lý
	private String previewUrl;

	// key trên cloudflare R2 (private)
	private String fileKey;

	// url hsl video
	private String hlsVideoUrl;

	private Long totalSales;

	private LocalDateTime createdAt;
	
	private Long width;
	
	private Long height;
	
	private Long duration;
	
	private String format;
	
	private String hash;
	
	private FileFormatEnum originalFileFormat; // ai, psd, png...
	
	private Long size;
	
	@Column(nullable = false)
	private Boolean deletedFlag;
	
	@PrePersist
	public void prePersist() {
	    createdAt = LocalDateTime.now();
	    totalSales = 0l;
	    deletedFlag = false;
	}
}
