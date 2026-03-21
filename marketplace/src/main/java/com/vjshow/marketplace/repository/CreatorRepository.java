package com.vjshow.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vjshow.marketplace.entity.CreatorRegister;

public interface CreatorRepository  extends JpaRepository<CreatorRegister, Long> {

}
