package com.vjshow.marketplace.mapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.vjshow.marketplace.dto.response.UserProductResponse;
import com.vjshow.marketplace.entity.ProductEntity;

@Component
public class ProductMapper {

	@Value("${storage.r2.public-url}")
	private String publicUrl;

	public UserProductResponse toResponse(ProductEntity p) {
		UserProductResponse res = new UserProductResponse();

		res.setId(p.getId());
		res.setName(p.getName());
		res.setPrice(p.getPrice());
		res.setStatus(p.getStatus() != null ? p.getStatus().name() : null);
		res.setType(p.getType());

		res.setThumbnailUrl(buildUrl(p.getThumbnailUrl()));
		res.setPreviewUrl(buildUrl(p.getPreviewUrl()));

		res.setCreatedAt(p.getCreatedAt());

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

		return res;
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
