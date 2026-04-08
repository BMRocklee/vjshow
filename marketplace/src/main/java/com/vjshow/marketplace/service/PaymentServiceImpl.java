package com.vjshow.marketplace.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.vjshow.marketplace.dto.response.PaymentStatusResponse;
import com.vjshow.marketplace.entity.OrderEntity;
import com.vjshow.marketplace.entity.PaymentEntity;
import com.vjshow.marketplace.enums.PaymentStatusEnum;
import com.vjshow.marketplace.exception.LogicException;
import com.vjshow.marketplace.repository.OrderRepository;
import com.vjshow.marketplace.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private final PaymentRepository paymentRepo;
	
	private final OrderRepository orderRepo;
		
	private final CloudFlareService storageService;

	@Override
	public void markSuccess(PaymentEntity payment) {
		paymentRepo.save(payment);
	}

	@Override
	public Optional<PaymentEntity> findByContent(String content) {
		// TODO Auto-generated method stub
		return paymentRepo.findByContentInText(content);
	}

	@Override
	public PaymentStatusResponse checkPayment(String paymentId, Long userId) {
		
		// 1. find payment
        PaymentEntity payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new LogicException("NOT_FOUND","không tìm thấy paymentID"));

       // 2. find order từ payment
        OrderEntity order = orderRepo.findByPaymentId(paymentId)
                .orElseThrow(() -> new LogicException("NOT_FOUND","không tìm thấy Order"));
        
        // 3. SECURITY - đúng user
        if (!order.getBuyer().getId().equals(userId)) {
            throw new LogicException("NOT_FOUND","bạn chưa có hóa đơn mua hàng");
        }
        
        // 4. EXPIRE PAYMENT (5 phút)
        if (payment.getStatus() == PaymentStatusEnum.PENDING) {

            if (payment.getCreatedAt()
                    .plusMinutes(5)
                    .isBefore(LocalDateTime.now())) {

                payment.setStatus(PaymentStatusEnum.EXPIRED);
                paymentRepo.save(payment);
            }
        }
        
        PaymentStatusResponse res = new PaymentStatusResponse();
        res.setStatus(payment.getStatus());
        // 5. nếu PAID → trả link download
        if (payment.getStatus() == PaymentStatusEnum.PAID) {
            // 🔥 check download hết hạn chưa
            if (order.getDownloadExpiredAt() != null &&
                order.getDownloadExpiredAt().isBefore(LocalDateTime.now())) {
                throw new LogicException("EX","đã hết thời hạn download");
            }

            // 👉 generate presigned
			String url = storageService.generateDownloadUrl(order.getProduct().getFileKey());
            res.setDownloadUrl(url);
        }

        return res;
	}

}
