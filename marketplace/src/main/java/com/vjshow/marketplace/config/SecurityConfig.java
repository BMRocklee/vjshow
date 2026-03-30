package com.vjshow.marketplace.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.vjshow.marketplace.OAuth2.OAuth2SuccessHandler;
import com.vjshow.marketplace.entity.UserEntity;
import com.vjshow.marketplace.filter.JwtAuthenticationFilter;
import com.vjshow.marketplace.repository.UserRepository;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtFilter;

	private final OAuth2SuccessHandler successHandler;

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// 🔥 2. UserDetailsService (BƯỚC 4 bạn hỏi)
	@Bean
	public UserDetailsService userDetailsService(UserRepository repo) {
	    return email -> {
	        UserEntity user = repo.findByEmail(email)
	                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

	        return new CustomUserDetails(user);
	    };
	}

	// 🔥 3. AuthenticationProvider
	@Bean
	public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder);

		return provider;
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(Customizer.withDefaults()).csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(
						auth -> auth
								.requestMatchers("/oauth2/**", "/api/auth/**", "/api/products/**", "/files/**",
										"/api/webhook/**", "/api/data-deletion").permitAll()
								.requestMatchers("/admin/**").hasRole("ADMIN")
								.anyRequest().authenticated())
				.exceptionHandling(ex -> ex.authenticationEntryPoint(
						(req, res, ex2) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED)))
				.oauth2Login(oauth -> oauth.successHandler(successHandler))
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();

	}
}
