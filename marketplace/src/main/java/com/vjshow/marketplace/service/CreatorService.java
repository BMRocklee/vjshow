package com.vjshow.marketplace.service;

import java.util.List;

import com.vjshow.marketplace.dto.request.CreatorApplyRequestDTO.CreatorApplyRequest;
import com.vjshow.marketplace.entity.CreatorEntity;
import com.vjshow.marketplace.entity.UserEntity;
import com.vjshow.marketplace.enums.CreatorStatus;

public interface CreatorService {
	void apply(UserEntity user, CreatorApplyRequest req);

	List<CreatorEntity> getAll();

	void approve(Long creatorId);

	void reject(Long creatorId);

	CreatorStatus getStatus(UserEntity user);
	
	List<CreatorEntity> findByStatus(CreatorStatus status);

    long countByStatus(CreatorStatus status);
}
