package com.vjshow.marketplace.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vjshow.marketplace.dto.response.PaymentStatusResponse;
import com.vjshow.marketplace.facade.PaymentFacade;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
	private final PaymentFacade paymentFacade;
	
	 @GetMapping("/{id}")
	    public PaymentStatusResponse check(Authentication authentication, @PathVariable String id) {
	        return paymentFacade.check(authentication, id);
	    }
}
