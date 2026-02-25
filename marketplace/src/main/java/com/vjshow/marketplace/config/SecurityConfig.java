package com.vjshow.marketplace.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.vjshow.marketplace.OAuth2.OAuth2SuccessHandler;
import com.vjshow.marketplace.filter.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtFilter;

	private final OAuth2SuccessHandler successHandler;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable())
				.sessionManagement(session ->
				session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				)
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(
								"/oauth2/**", "/auth/**"
						).permitAll()
						.anyRequest()
						.authenticated()
				)
				.oauth2Login(oauth -> oauth
						.successHandler(successHandler)
				)
				.addFilterBefore(
						jwtFilter,
						UsernamePasswordAuthenticationFilter.class
				);

		return http.build();

	}
}
