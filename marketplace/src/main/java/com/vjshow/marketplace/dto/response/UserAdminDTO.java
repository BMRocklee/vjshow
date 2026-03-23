package com.vjshow.marketplace.dto.response;

import java.time.LocalDateTime;

import com.vjshow.marketplace.enums.UserStatusEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAdminDTO {
	private Long id;

    private String name;

    private String email;

    private String avatar;

    private String uid;

    private String role;

    private UserStatusEnum status;

    private LocalDateTime createdAt;

    private Double totalSpent; // optional (nếu muốn hiển thị sau)
}
