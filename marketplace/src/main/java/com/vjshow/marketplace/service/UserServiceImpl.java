package com.vjshow.marketplace.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.vjshow.marketplace.dto.response.UserAdminDTO;
import com.vjshow.marketplace.dto.response.UserAdminResponse;
import com.vjshow.marketplace.dto.response.UserStatsDTO;
import com.vjshow.marketplace.entity.UserEntity;
import com.vjshow.marketplace.enums.AuthProviderEnum;
import com.vjshow.marketplace.enums.Role;
import com.vjshow.marketplace.enums.UserStatusEnum;
import com.vjshow.marketplace.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Override
	public UserEntity findOrCreateOAuthUser(AuthProviderEnum provider, String providerId, String email, String name,
			String picture) {
		return userRepository.findByProviderAndProviderId(provider, providerId)
				.orElseGet(() -> createUser(provider, providerId, email, name, picture));
	}

	private UserEntity createUser(AuthProviderEnum provider, String providerId, String email, String name, String picture) {
		UserEntity user = UserEntity.builder().provider(provider).providerId(providerId).email(email).name(name).picture(picture)
				.role(Role.USER).build();
		return userRepository.save(user);
	}

	@Override
	public long getTotalUsers() {
		return userRepository.count();
	}

	@Override
	public UserAdminResponse getUsers(String keyword, UserStatusEnum status) {
		List<UserEntity> users;

	    boolean hasKeyword = keyword != null && !keyword.isBlank();

	    if (status != null && hasKeyword) {
	        users = userRepository.findByStatusAndNameContainingIgnoreCase(status, keyword);
	    } else if (status != null) {
	        users = userRepository.findByStatus(status);
	    } else if (hasKeyword) {
	        users = userRepository.findByNameContainingIgnoreCase(keyword);
	    } else {
	        users = userRepository.findAll();
	    }

	    List<UserAdminDTO> list = users.stream().map(user ->
	        UserAdminDTO.builder()
	            .id(user.getId())
	            .name(user.getName())
	            .email(user.getEmail())
	            .avatar(user.getPicture())
	            .uid(user.getPublicId().toString())
	            .role(user.getRole().name())
	            .createdAt(user.getCreatedAt())
	            .status(user.getStatus())
	            .build()
	    ).toList();

	    // stats
	    UserStatsDTO stats = UserStatsDTO.builder()
	        .totalUsers(userRepository.count())
	        .active(userRepository.countByStatus(UserStatusEnum.ACTIVE))
	        .blocked(userRepository.countByStatus(UserStatusEnum.BLOCKED))
	        .build();

	    return new UserAdminResponse(stats, list);
	}
}
