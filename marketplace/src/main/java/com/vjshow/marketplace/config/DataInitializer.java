package com.vjshow.marketplace.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vjshow.marketplace.entity.CreatorEntity;
import com.vjshow.marketplace.entity.UserEntity;
import com.vjshow.marketplace.enums.CreatorStatus;
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
                admin.setRole(Role.ADMIN);
                admin.setStatus(UserStatusEnum.ACTIVE);

                userRepository.save(admin);
            }

            // ===== CREATOR =====
            if (userRepository.findByEmail("creator@vjshow.vn").isEmpty()) {

                // tạo user
            	UserEntity creatorUser = new UserEntity();
                creatorUser.setEmail("creator@vjshow.vn");
                creatorUser.setName("Dummy Creator");
                creatorUser.setPassword(passwordEncoder.encode("123456"));
                creatorUser.setRole(Role.CREATOR);
                creatorUser.setStatus(UserStatusEnum.ACTIVE);

                userRepository.save(creatorUser);

                // tạo creator profile
                CreatorEntity creator = new CreatorEntity();
                creator.setUser(creatorUser);
                creator.setEmail("creator@vjshow.vn");
                creator.setStatus(CreatorStatus.APPROVED);
                creatorRepository.save(creator);
            }
        };
    }

}