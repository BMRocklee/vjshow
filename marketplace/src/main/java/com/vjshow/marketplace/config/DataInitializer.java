package com.vjshow.marketplace.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vjshow.marketplace.entity.UserEntity;
import com.vjshow.marketplace.enums.Role;
import com.vjshow.marketplace.enums.UserStatusEnum;
import com.vjshow.marketplace.repository.CreatorRepository;
import com.vjshow.marketplace.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository,
                                      CreatorRepository creatorRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {

            // ===== ADMIN =====
            if (userRepository.findByEmail("admin@vjshow.vn").isEmpty()) {
                UserEntity admin = new UserEntity();
                admin.setEmail("admin@vjshow.vn");
                admin.setName("Admin");
                admin.setPassword(passwordEncoder.encode("123456"));
                admin.setPicture("https://cdn-icons-png.flaticon.com/512/149/149071.png");
                admin.setRole(Role.ADMIN);
                admin.setStatus(UserStatusEnum.ACTIVE);

                userRepository.save(admin);
            }

        };
    }

}
