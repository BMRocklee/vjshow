package com.vjshow.marketplace.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreatorAdminResponseDTO {
	private Long CreatorId;
	private String username;
	private String avatar;
	private String uid;
	private int totalProducts;
	private long revenue;
	private long commission;
	private String status;

}
