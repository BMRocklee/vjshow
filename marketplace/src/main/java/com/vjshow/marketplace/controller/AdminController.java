package com.vjshow.marketplace.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vjshow.marketplace.dto.response.AdminDashboardResponseDto;
import com.vjshow.marketplace.dto.response.CreatorPaymentsResponse;
import com.vjshow.marketplace.dto.response.CreatorRegisterInfoResponse;
import com.vjshow.marketplace.dto.response.UserAdminResponse;
import com.vjshow.marketplace.enums.CreatorStatus;
import com.vjshow.marketplace.enums.UserStatusEnum;
import com.vjshow.marketplace.facade.AdminFacade;
import com.vjshow.marketplace.facade.WalletFacade;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

	private final AdminFacade adminFacade;
	
	private final WalletFacade facade;

	@GetMapping("/stats")
	public ResponseEntity<?> getAdminDashboardInfo() {

		AdminDashboardResponseDto adminInfor = adminFacade.getAdminDashboardStatInfo();

		return ResponseEntity.ok(adminInfor);

	}

	@GetMapping("/creators")
	public ResponseEntity<?> getCreatorDashboard(@RequestParam(required = false) CreatorStatus status,
			@RequestParam(required = false) String keyword) {

		CreatorRegisterInfoResponse adminCreatorInfor = adminFacade.getCreatorDashboard(status, keyword);

		return ResponseEntity.ok(adminCreatorInfor);
	}
	
	@PostMapping("/creators/{id}/approve")
	public ResponseEntity<?> approve(@PathVariable Long id) {
		adminFacade.approveCreator(id);
	    return ResponseEntity.ok().build();
	}
	
	@PostMapping("/creators/{id}/reject")
	public ResponseEntity<?> reject(@PathVariable Long id) {
		adminFacade.rejectCreator(id);
	    return ResponseEntity.ok().build();
	}

	@GetMapping("/payments")
	public ResponseEntity<?> getCreatorPayment() {

		CreatorPaymentsResponse adminCreatorInfor = adminFacade.getCreatorPayments();

		return ResponseEntity.ok(adminCreatorInfor);
	}
	
	@GetMapping("/users")
	public ResponseEntity<?> getUserList(@RequestParam(required = false) String keyword,
	        @RequestParam(required = false) UserStatusEnum status) {

		UserAdminResponse adminUserInfor = adminFacade.getUserList(keyword, status);

		return ResponseEntity.ok(adminUserInfor);
	}
	
	@PostMapping("/users/{id}/block")
	public ResponseEntity<?> block(@PathVariable Long id) {
		adminFacade.blockUser(id);
	    return ResponseEntity.ok().build();
	}

	@PostMapping("/users/{id}/active")
	public ResponseEntity<?> active(@PathVariable Long id) {
		adminFacade.activeUser(id);
	    return ResponseEntity.ok().build();
	}
	
	@PostMapping("/wallet/withdraw/{id}/approve")
    public ResponseEntity<?> approve(@PathVariable String id) {
        facade.adminApproveWithdraw(id);
        return ResponseEntity.ok().build();
    }
}
