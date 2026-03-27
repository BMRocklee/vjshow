package com.vjshow.marketplace.facade;

import com.vjshow.marketplace.dto.request.CompleteUploadRequest;
import com.vjshow.marketplace.dto.response.UploadResponse;
import com.vjshow.marketplace.entity.ProductEntity;

public interface UploadFacade {
	UploadResponse initUpload(String fileName, Long fileSize, Long userId);

    ProductEntity completeUpload(CompleteUploadRequest request, Long userId);
}
