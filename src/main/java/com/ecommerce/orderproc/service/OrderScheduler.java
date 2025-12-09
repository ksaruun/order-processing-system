package com.ecommerce.orderproc.service;

import com.ecommerce.orderproc.model.Order;
import com.ecommerce.orderproc.model.OrderStatus;
import com.ecommerce.orderproc.repository.OrderRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class OrderScheduler {

    private final OrderRepository orderRepository;
    private final long fixedInterval = 60000; // 5 minutes in milliseconds

    public OrderScheduler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Runs every 5 minutes (300000 milliseconds)
    @Scheduled(fixedRate = fixedInterval)
    @Transactional
    public void processPendingOrders() {
        List<Order> pendingOrders = orderRepository.findByStatus(OrderStatus.PENDING);
        pendingOrders.stream()
                .filter(order -> order.getCreatedAt().toEpochMilli() < (System.currentTimeMillis() - fixedInterval))
                .forEach(order -> order.setStatus(OrderStatus.PROCESSING));

    }
}