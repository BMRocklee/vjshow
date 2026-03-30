package com.vjshow.marketplace.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vjshow.marketplace.config.CustomUserDetails;
import com.vjshow.marketplace.dto.request.LoginRequest;
import com.vjshow.marketplace.dto.response.AuthResponse;
import com.vjshow.marketplace.entity.UserEntity;
import com.vjshow.marketplace.enums.Role;
import com.vjshow.marketplace.exception.LogicException;
import com.vjshow.marketplace.service.JwtService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	
    private final AuthenticationManager authenticationManager;
    
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        UserEntity user = userDetails.getUser(); // 🔥 lấy entity thật

        if (!user.getRole().equals(Role.ADMIN)) {
            throw new LogicException("ACCESS_DENY","không có quyền truy cập");
        }
        
        String accessToken = jwtService.generateAccessToken(user);

	    long expiresIn = jwtService.getAccessTokenExpiration();

	    AuthResponse res = AuthResponse.builder()
	            .accessToken(accessToken)
	            .tokenType("Bearer")
	            .expiresIn(expiresIn)
	            .build();

        return ResponseEntity.ok(res);
    }
}
