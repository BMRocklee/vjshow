package com.vjshow.marketplace.facade;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.vjshow.marketplace.dto.response.PurchaseItemResponse;
import com.vjshow.marketplace.entity.OrderEntity;
import com.vjshow.marketplace.entity.ProductEntity;
import com.vjshow.marketplace.enums.OrderStatusEnum;
import com.vjshow.marketplace.service.CloudFlareService;
import com.vjshow.marketplace.service.OrderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PurchaseFacadeImpl implements PurchaseFacade {

    private final OrderService orderService;
    private final CloudFlareService cloudFlareService;

    @Override
    public List<PurchaseItemResponse> getMyPurchases(Long userId) {

        List<OrderEntity> orders = orderService.getPaidOrders(userId);

        return orders.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public String downloadProduct(Long userId, Long orderId) {

        OrderEntity order = orderService.getOrderById(orderId);

        validateOwnership(order, userId);
        validateDownload(order);

        return cloudFlareService.generateDownloadUrl(
                order.getProduct().getFileKey()
        );
    }

    // ================= PRIVATE =================

    private PurchaseItemResponse mapToResponse(OrderEntity order) {
        ProductEntity p = order.getProduct();

        return PurchaseItemResponse.builder()
                .orderId(order.getId())
                .productId(p.getId())
                .name(p.getName())
                .thumbnail(p.getThumbnailUrl())
                .price(order.getAmount())
                .purchasedAt(order.getPaidAt())
                .canDownload(canDownload(order))
                .build();
    }

    private boolean canDownload(OrderEntity order) {
        return order.getDownloadExpiredAt() == null ||
                order.getDownloadExpiredAt().isAfter(LocalDateTime.now());
    }

    private void validateOwnership(OrderEntity order, Long userId) {
        if (!order.getBuyer().getId().equals(userId)) {
            throw new RuntimeException("Forbidden");
        }
    }

    private void validateDownload(OrderEntity order) {

        if (order.getStatus() != OrderStatusEnum.PAID) {
            throw new RuntimeException("Order not paid");
        }

        if (!canDownload(order)) {
            throw new RuntimeException("Download expired");
        }
    }
}
