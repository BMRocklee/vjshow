package com.vjshow.marketplace.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vjshow.marketplace.dto.response.CreatorDashboardResponse;
import com.vjshow.marketplace.dto.response.AdminDashboardResponseDto;
import com.vjshow.marketplace.facade.AdminFacade;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

	private final AdminFacade adminFacade;

	@GetMapping("/stats")
	public ResponseEntity<?> getAdminDashboardInfo() {

		AdminDashboardResponseDto adminInfor = adminFacade.getAdminDashboardStatInfo();

		return ResponseEntity.ok(adminInfor);

	}
	
	@GetMapping("/creators")
	public ResponseEntity<?> getCreatorDashboard() {

		CreatorDashboardResponse adminCreatorInfor = adminFacade.getCreatorDashboard();

		return ResponseEntity.ok(adminCreatorInfor);
	}
}