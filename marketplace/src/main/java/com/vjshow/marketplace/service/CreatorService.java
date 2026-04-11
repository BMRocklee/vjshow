package com.vjshow.marketplace.service;

import java.util.List;
import java.util.UUID;

import com.vjshow.marketplace.dto.request.CreatorApplyRequestDTO.CreatorApplyRequest;
import com.vjshow.marketplace.dto.response.CreatorResponse;
import com.vjshow.marketplace.entity.CreatorEntity;
import com.vjshow.marketplace.entity.UserEntity;
import com.vjshow.marketplace.enums.CreatorStatus;

public interface CreatorService {
	public void apply(UserEntity user, CreatorApplyRequest req);

	public List<CreatorEntity> getAll();

	public void approve(Long creatorId);

	public void reject(Long creatorId);

	public CreatorStatus getStatus(UserEntity user);
	
	public List<CreatorEntity> findByStatus(CreatorStatus status);

	public long countByStatus(CreatorStatus status);

    public CreatorEntity getByUserId(Long userId);

	public void checkQuota(CreatorEntity creator, Long fileSize);

    public void increaseStorage(CreatorEntity creator, Long fileSize);
    
    public void decreaseStorage(CreatorEntity creator, Long fileSize);
    
    public CreatorResponse getByPublicId(UUID publicId);
}
