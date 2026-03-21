package com.vjshow.marketplace.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "creator-register")
public class CreatorRegister {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private String name;

    private String email;

    private String phone;

    private String type;

    private String company;

    private String status; // PENDING APPROVED REJECTED

    private LocalDateTime createdAt;

    private LocalDateTime reviewedAt;
}