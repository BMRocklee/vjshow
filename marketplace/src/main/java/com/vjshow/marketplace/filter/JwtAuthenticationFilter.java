package com.vjshow.marketplace.filter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.vjshow.marketplace.entity.User;
import com.vjshow.marketplace.repository.UserRepository;
import com.vjshow.marketplace.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
    private final UserRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String header = request.getHeader("Authorization");

		if (header == null || !header.startsWith("Bearer ")) {

			filterChain.doFilter(request, response);

			return;

		}

		String token = header.substring(7);

		if (!jwtService.isValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }
		
		 UUID userId = jwtService.extractUserId(token);

	        User user = userRepository.findByPublicId(userId).orElse(null);

	        if (user != null) {

	            UsernamePasswordAuthenticationToken auth =
	                    new UsernamePasswordAuthenticationToken(
	                            user,
	                            null,
	                            List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
	                    );

	            SecurityContextHolder.getContext().setAuthentication(auth);
	        }

		

		filterChain.doFilter(request, response);
	}

}
