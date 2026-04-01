package com.vjshow.marketplace.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.vjshow.marketplace.entity.OrderEntity;
import com.vjshow.marketplace.entity.PaymentEntity;
import com.vjshow.marketplace.entity.ProductEntity;
import com.vjshow.marketplace.entity.UserEntity;
import com.vjshow.marketplace.enums.OrderStatusEnum;
import com.vjshow.marketplace.enums.PaymentStatusEnum;
import com.vjshow.marketplace.exception.LogicException;
import com.vjshow.marketplace.repository.OrderRepository;
import com.vjshow.marketplace.repository.PaymentRepository;
import com.vjshow.marketplace.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
	
	private final ProductRepository productRepo;
    private final OrderRepository orderRepo;
    private final PaymentRepository paymentRepo;

	@Override
	public PaymentEntity createNewOrder(Long productId, UserEntity buyer) {
		ProductEntity product = productRepo.findById(productId)
                .orElseThrow(() -> new LogicException("NOT_FOUND", "Không tìm thấy sản phẩm"));
		
		  // 🔍 tìm order đang pending
	    Optional<OrderEntity> existing = orderRepo.findTopByBuyerIdAndProductIdOrderByCreatedAtDesc(buyer.getId(), productId);
	    
	    if (existing.isPresent()) {
	    	OrderEntity order = existing.get();
	    	
	    	if (order.getStatus() == OrderStatusEnum.PAID) {
	            throw new LogicException("BUYED", "Bạn đã mua sản phẩm này");
	        }
	    	
	    	if (order.getStatus() == OrderStatusEnum.PENDING) {
	    		if (order.getPayment() != null) {
	    	        return order.getPayment();
	    	    }
	        }
	    }
        
        
        // 2. Create payment
        String paymentId = UUID.randomUUID().toString();

        PaymentEntity payment = new PaymentEntity();
        payment.setId(paymentId);
        payment.setAmount(product.getPrice());
        payment.setContent("VJSHOW-" + paymentId);
        payment.setStatus(PaymentStatusEnum.PENDING);
        payment.setCreatedAt(LocalDateTime.now());
        paymentRepo.save(payment);
        

        // 3. tạo order
        OrderEntity order = new OrderEntity();
        order.setBuyer(buyer);
        order.setCreator(product.getCreator());
        order.setProduct(product);

        order.setPayment(payment);
        order.setAmount(product.getPrice());

        long commission = (long) (product.getPrice() * 0.22);
        order.setCommission(commission);

        order.setStatus(OrderStatusEnum.PENDING);
        order.setPaymentMethod("QRCODE");
        order.setCreatedAt(LocalDateTime.now());

        orderRepo.save(order);
        return payment;
	}

	@Override
	public Optional<OrderEntity> findPending(Long buyerId, Long productId) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public void markPaid(OrderEntity order) {
        order.setStatus(OrderStatusEnum.PAID);
        order.setPaidAt(LocalDateTime.now());
		orderRepo.save(order);
	}

}
