package com.vjshow.marketplace.service;

import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.vjshow.marketplace.entity.User;
import com.vjshow.marketplace.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
	private final UserRepository userRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest request) {
		OAuth2User oauthUser = super.loadUser(request);

		Map<String, Object> attr = oauthUser.getAttributes();

		String email = (String) attr.get("email");
		String name = (String) attr.get("name");
		String picture = (String) attr.get("picture");
		String providerId = (String) attr.get("sub");

//		User user = userRepository.findByEmail(email).orElse(User.builder().email(email).name(name).provider("GOOGLE")
//				.providerId(providerId).picture(picture).build());

//		userRepository.save(user);

		return oauthUser;
	}
}
