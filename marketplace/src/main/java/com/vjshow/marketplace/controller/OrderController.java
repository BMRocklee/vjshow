package com.vjshow.marketplace.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vjshow.marketplace.dto.request.CreateOrderRequestDto;
import com.vjshow.marketplace.dto.response.CreatePaymentResponse;
import com.vjshow.marketplace.entity.UserEntity;
import com.vjshow.marketplace.facade.OrderFacade;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
	private final OrderFacade orderFacade;
	
	@PostMapping("/create")
    public CreatePaymentResponse create(Authentication authentication, @RequestBody CreateOrderRequestDto orderDto) {

        UserEntity currentUser = (UserEntity) authentication.getPrincipal();

        return orderFacade.createOrder(orderDto.getProductId(), currentUser);
    }
}
