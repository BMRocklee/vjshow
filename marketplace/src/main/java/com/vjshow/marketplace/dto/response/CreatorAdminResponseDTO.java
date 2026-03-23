package com.vjshow.marketplace.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreatorAdminResponseDTO {
	private Long id;
	private String username;
	private String avatar;
	private String uid;
	private String phone;
	private String company;
	private String status;
}
