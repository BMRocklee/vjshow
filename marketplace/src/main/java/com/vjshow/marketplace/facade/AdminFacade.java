package com.vjshow.marketplace.facade;

import com.vjshow.marketplace.dto.response.CreatorDashboardResponse;
import com.vjshow.marketplace.dto.response.AdminDashboardResponseDto;

public interface AdminFacade {
  public AdminDashboardResponseDto getAdminDashboardStatInfo();

  public CreatorDashboardResponse getCreatorDashboard(); 
}
