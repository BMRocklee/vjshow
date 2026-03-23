package com.vjshow.marketplace.facade;

import java.util.List;

import org.springframework.stereotype.Component;

import com.vjshow.marketplace.dto.response.AdminDashboardResponseDto;
import com.vjshow.marketplace.dto.response.CreatorAdminResponseDTO;
import com.vjshow.marketplace.dto.response.CreatorPaymentInfoDTO;
import com.vjshow.marketplace.dto.response.CreatorPaymentsResponse;
import com.vjshow.marketplace.dto.response.CreatorRegisterInfoResponse;
import com.vjshow.marketplace.dto.response.CreatorStatsDTO;
import com.vjshow.marketplace.dto.response.UserAdminResponse;
import com.vjshow.marketplace.entity.CreatorEntity;
import com.vjshow.marketplace.entity.UserEntity;
import com.vjshow.marketplace.enums.CreatorStatus;
import com.vjshow.marketplace.enums.UserStatusEnum;
import com.vjshow.marketplace.exception.LogicException;
import com.vjshow.marketplace.repository.CreatorRepository;
import com.vjshow.marketplace.repository.UserRepository;
import com.vjshow.marketplace.service.CreatorService;
import com.vjshow.marketplace.service.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminFacadeImpl implements AdminFacade {

	private final UserService userService;

	private final CreatorService creatorService;

	private final CreatorRepository creatorRepository;
	
	private final UserRepository userRepository;

	@Override
	public AdminDashboardResponseDto getAdminDashboardStatInfo() {
		long totalUsers = userService.getTotalUsers();

		long pendingCreators = creatorService.countByStatus(CreatorStatus.PENDING);
		long approvedCreators = creatorService.countByStatus(CreatorStatus.APPROVED);

		AdminDashboardResponseDto adminInfo = AdminDashboardResponseDto.builder().totalUsers(totalUsers)
				.totalCreators(approvedCreators).totalCreatorsPending(pendingCreators).build();

		return adminInfo;
	}

	@Override
	public CreatorRegisterInfoResponse getCreatorDashboard(CreatorStatus status, String keyword) {
		 List<CreatorEntity> creators;

		 boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();

		 if (status != null && hasKeyword) {
		        creators = creatorRepository
		                .findByStatusAndUser_NameContainingIgnoreCase(status, keyword);

		    } else if (status != null) {
		        creators = creatorRepository.findByStatus(status);

		    } else if (hasKeyword) {
		        creators = creatorRepository.findByUser_NameContainingIgnoreCase(keyword);

		    } else {
		        creators = creatorRepository.findAll();
		    }

		    // 👉 map list
		    List<CreatorAdminResponseDTO> creatorList = creators.stream().map(creator -> {
		        UserEntity user = creator.getUser();

		        return CreatorAdminResponseDTO.builder()
		                .id(creator.getId())
		                .username(user.getName())
		                .avatar(user.getPicture())
		                .uid(user.getPublicId().toString())
		                .phone(creator.getPhone())
		                .company(creator.getCompany())
		                .status(creator.getStatus().name())
		                .build();
		    }).toList();
		    
		    long approvedCreators = creatorRepository.countByStatus(CreatorStatus.APPROVED);
			long pendingCreators = creatorRepository.countByStatus(CreatorStatus.PENDING);
			long rejectCreators = creatorRepository.countByStatus(CreatorStatus.REJECTED);

		    // 👉 stats (dựa trên list hiện tại)
		    CreatorStatsDTO stats = CreatorStatsDTO.builder()
		            .totalCreators(creatorList.size())
		            .totalPendingCreators(pendingCreators)
		            .totalApprovedCreators(approvedCreators)
		            .totalRejectCreators(rejectCreators)
		            .build();

		    // 👉 return object
		    return CreatorRegisterInfoResponse.builder()
		            .stats(stats)
		            .creators(creatorList)
		            .build();
	}

	@Override
	public CreatorPaymentsResponse getCreatorPayments() {
		long totalCreators = creatorRepository.countByStatus(CreatorStatus.APPROVED);

		List<CreatorPaymentInfoDTO> creators = creatorRepository.findByStatus(CreatorStatus.APPROVED).stream().map(creator -> {
			UserEntity user = creator.getUser();

			return CreatorPaymentInfoDTO.builder()
					.id(creator.getId())
					.username(user.getName())
					.avatar(user.getPicture())
					.uid(user.getPublicId().toString())
					.totalProducts(0)
					.revenue(0)
					.commission(0)
					.status(creator.getStatus().name())
					.build();
		}).toList();
		
		CreatorStatsDTO stats = CreatorStatsDTO.builder().totalCreators(totalCreators)
				.totalRevenue(0).totalCommission(0).build();

		return CreatorPaymentsResponse.builder().stats(stats).creators(creators).build();
	}

	@Override
	public void approveCreator(Long creatorId) {
		creatorService.approve(creatorId);
	}

	@Override
	public void rejectCreator(Long creatorId) {
		creatorService.reject(creatorId);
	}

	@Override
	public UserAdminResponse getUserList(String keyword, UserStatusEnum status) {
		return userService.getUsers(keyword, status);
	}

	@Override
	public void blockUser(Long id) {
		UserEntity user = userRepository.findById(id)
				.orElseThrow(() -> new LogicException("USER_NOT_FOUND", "User không tồn tại"));
		user.setStatus(UserStatusEnum.BLOCKED);
		userRepository.save(user);
	}

	@Override
	public void activeUser(Long id) {
		UserEntity user = userRepository.findById(id)
				.orElseThrow(() -> new LogicException("USER_NOT_FOUND", "User không tồn tại"));
		user.setStatus(UserStatusEnum.ACTIVE);
		userRepository.save(user);
	}
}
