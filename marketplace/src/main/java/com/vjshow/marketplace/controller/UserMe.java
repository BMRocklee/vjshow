package com.vjshow.marketplace.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vjshow.marketplace.entity.User;

@RestController
@RequestMapping("/api")
public class UserMe {

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.ok(
                Map.of("authenticated", false)
            );
        }

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
            Map.of(
                "id", user.getPublicId(),
                "email", user.getEmail(),
                "name", user.getName(),
                "picture", user.getPicture(),
                "role", user.getRole()
            )
        );
    }
}