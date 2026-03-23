package com.vjshow.marketplace.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatorAdminDTO {
    private Long id;
    private String username;
    private String avatar;
    private String uid;
    private int totalProducts;
    private long revenue;
    private long commission;
    private String status;
}
