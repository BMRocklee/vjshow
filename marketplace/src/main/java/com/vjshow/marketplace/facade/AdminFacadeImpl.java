package com.vjshow.marketplace.facade;

import java.util.List;

import org.springframework.stereotype.Component;

import com.vjshow.marketplace.dto.response.AdminDashboardResponseDto;
import com.vjshow.marketplace.dto.response.CreatorAdminDTO;
import com.vjshow.marketplace.dto.response.CreatorDashboardResponse;
import com.vjshow.marketplace.dto.response.CreatorStatsDTO;
import com.vjshow.marketplace.entity.UserEntity;
import com.vjshow.marketplace.enums.CreatorStatus;
import com.vjshow.marketplace.repository.CreatorRepository;
import com.vjshow.marketplace.service.CreatorService;
import com.vjshow.marketplace.service.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminFacadeImpl implements AdminFacade {

	private final UserService userService;

	private final CreatorService creatorService;

	private final CreatorRepository creatorRepository;

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
	public CreatorDashboardResponse getCreatorDashboard() {

		long totalCreators = creatorRepository.countByStatus(CreatorStatus.APPROVED);

		long pendingCreators = creatorRepository.countByStatus(CreatorStatus.PENDING);

		List<CreatorAdminDTO> creators = creatorRepository.findAll().stream().map(c -> {
			UserEntity user = c.getUser();

			return CreatorAdminDTO.builder()
					.id(c.getId())
					.username(user.getName())
					.avatar(user.getPicture())
					.uid(user.getPublicId().toString())
					.totalProducts(0)
					.revenue(0)
					.commission(0)
					.status(c.getStatus() != null ? c.getStatus().name() : "")
					.build();
		}).toList();
		
		CreatorStatsDTO stats = CreatorStatsDTO.builder().totalCreators(totalCreators).pendingCreators(pendingCreators)
				.totalRevenue(0).totalCommission(0).build();

		return CreatorDashboardResponse.builder().stats(stats).creators(creators).build();
	}

}
