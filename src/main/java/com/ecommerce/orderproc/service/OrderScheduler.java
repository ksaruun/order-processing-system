package com.ecommerce.orderproc.service;

import com.ecommerce.orderproc.model.Order;
import com.ecommerce.orderproc.model.OrderStatus;
import com.ecommerce.orderproc.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderScheduler {

    private final OrderRepository orderRepository;
    private final long fixedInterval = 300000; // 5 minutes in milliseconds

    // Runs every 5 minutes (300000 milliseconds)
    @Scheduled(fixedRate = fixedInterval)
    @Transactional
    public void processPendingOrders() {
        List<Order> pending = orderRepository.findByStatus(OrderStatus.PENDING);
        for (Order order : pending) {
            order.nextState(); // Moves to ProcessingState
            orderRepository.save(order);
        }
    }
}
