package com.vjshow.marketplace.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.vjshow.marketplace.dto.request.CreatorApplyRequestDTO.CreatorApplyRequest;
import com.vjshow.marketplace.entity.CreatorEntity;
import com.vjshow.marketplace.entity.UserEntity;
import com.vjshow.marketplace.enums.CreatorStatus;
import com.vjshow.marketplace.enums.Role;
import com.vjshow.marketplace.exception.LogicException;
import com.vjshow.marketplace.repository.CreatorRepository;
import com.vjshow.marketplace.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreatorServiceImpl implements CreatorService {
	private final CreatorRepository creatorRepository;
	private final UserRepository userRepository;

	@Override
	public void apply(UserEntity user, CreatorApplyRequest req) {

		if (creatorRepository.existsByUserAndStatus(user, CreatorStatus.PENDING)) {
			throw new LogicException("DUPLICATE_REQUEST", "Bạn đã gửi yêu cầu trước đó rồi");
		}

		// TODO Auto-generated method stub
		CreatorEntity newCreator = CreatorEntity.builder().name(req.name()).email(req.email()).phone(req.phone()).type(req.type())
				.company(req.company()).user(user).status(CreatorStatus.PENDING).createdAt(LocalDateTime.now()).build();

		creatorRepository.save(newCreator);
	}

	@Override
	public List<CreatorEntity> getAll() {
		return creatorRepository.findAllByOrderByCreatedAtDesc();
	}

	@Override
	public void approve(Long creatorId) {
		CreatorEntity creator = creatorRepository.findById(creatorId)
				.orElseThrow(() -> new RuntimeException("Not found"));

		creator.setStatus(CreatorStatus.APPROVED);

		UserEntity user = creator.getUser();
		user.setRole(Role.CREATOR);

		userRepository.save(user);
		creatorRepository.save(creator);
	}

	@Override
	public void reject(Long id) {
		CreatorEntity app = creatorRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy"));

		app.setStatus(CreatorStatus.REJECTED);
		creatorRepository.save(app);

	}

	@Override
	public CreatorStatus getStatus(UserEntity user) {
		return creatorRepository.findTopByUserOrderByCreatedAtDesc(user).map(CreatorEntity::getStatus).orElse(null);
	}

	@Override
	public List<CreatorEntity> findByStatus(CreatorStatus status) {
		return creatorRepository.findByStatus(status);
	}

	@Override
	public long countByStatus(CreatorStatus status) {
		// TODO Auto-generated method stub
		return creatorRepository.countByStatus(status);
	}
}
