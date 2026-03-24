package com.vjshow.marketplace.facade;

import com.vjshow.marketplace.dto.response.AdminDashboardResponseDto;
import com.vjshow.marketplace.dto.response.CreatorPaymentsResponse;
import com.vjshow.marketplace.dto.response.CreatorRegisterInfoResponse;
import com.vjshow.marketplace.dto.response.UserAdminResponse;
import com.vjshow.marketplace.enums.CreatorStatus;
import com.vjshow.marketplace.enums.UserStatusEnum;

public interface AdminFacade {
	public AdminDashboardResponseDto getAdminDashboardStatInfo();

	public CreatorRegisterInfoResponse getCreatorDashboard(CreatorStatus status, String keyword);

	public CreatorPaymentsResponse getCreatorPayments();

	public void approveCreator(Long creatorId);

	public void rejectCreator(Long creatorId);

	public UserAdminResponse getUserList(String keyword, UserStatusEnum status);

	public void blockUser(Long id);

	public void activeUser(Long id);
}
