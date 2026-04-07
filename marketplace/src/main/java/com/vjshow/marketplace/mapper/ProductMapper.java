package com.vjshow.marketplace.mapper;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.vjshow.marketplace.dto.response.OwnerResponse;
import com.vjshow.marketplace.dto.response.PurchaseItemResponse;
import com.vjshow.marketplace.dto.response.UserProductResponse;
import com.vjshow.marketplace.entity.OrderEntity;
import com.vjshow.marketplace.entity.ProductEntity;
import com.vjshow.marketplace.entity.UserEntity;

@Component
public class ProductMapper {

	@Value("${storage.r2.public-url}")
	private String publicUrl;

	public UserProductResponse toResponse(ProductEntity p) {
		UserProductResponse res = new UserProductResponse();
		
		UserEntity user = p.getCreator().getUser(); // tuỳ cấu trúc của bạn
		OwnerResponse owner = OwnerResponse.builder()
	            .id(user.getId())
	            .publicId(user.getPublicId())
	            .name(user.getName())
	            .picture(user.getPicture())
	            .build();

		res.setId(p.getId());
		res.setName(p.getName());
		res.setPrice(p.getPrice());
		res.setStatus(p.getStatus() != null ? p.getStatus().name() : null);
		res.setType(p.getType());

		res.setThumbnailUrl(buildUrl(p.getThumbnailUrl()));
		res.setPreviewUrl(buildUrl(p.getPreviewUrl()));
		res.setHlsUrl(buildUrl(p.getHlsVideoUrl()));

		res.setCreatedAt(p.getCreatedAt());
		res.setTotalSales(p.getTotalSales());

		// 🔥 media
		res.setWidth(p.getWidth());
		res.setHeight(p.getHeight());
		res.setDuration(p.getDuration());
		res.setSize(p.getSize());
		res.setFormat(p.getFormat());

		// 🔥 computed
		if (p.getWidth() != null && p.getHeight() != null) {
			res.setResolution(p.getWidth() + "x" + p.getHeight());
		}

		if (p.getSize() != null) {
			res.setSizeText(formatSize(p.getSize()));
		}
		
		res.setOwner(owner);

		return res;
	}
	
    // ================= PRIVATE =================

	public PurchaseItemResponse mapToResponse(OrderEntity order) {
        ProductEntity p = order.getProduct();

        return PurchaseItemResponse.builder()
                .orderId(order.getId())
                .productId(p.getId())
                .name(p.getName())
                .thumbnail(buildUrl(p.getThumbnailUrl()))
                .price(order.getAmount())
                .purchasedAt(order.getPaidAt())
                .canDownload(canDownload(order))
                .build();
    }
	
    private boolean canDownload(OrderEntity order) {
        return order.getDownloadExpiredAt() == null ||
                order.getDownloadExpiredAt().isAfter(LocalDateTime.now());
    }

	private String buildUrl(String path) {
		if (path == null)
			return null;

		// nếu đã là full URL thì giữ nguyên
		if (path.startsWith("http"))
			return path;

		return publicUrl.replaceAll("/$", "") + "/" + path.replaceAll("^/", "");
	}

	private String formatSize(Long size) {
		if (size == null)
			return null;

		double kb = size / 1024.0;
		double mb = kb / 1024.0;

		if (mb >= 1)
			return String.format("%.1f MB", mb);
		return String.format("%.0f KB", kb);
	}
}
