package com.ecommerce.orderproc.service;

import com.ecommerce.orderproc.model.*;
import com.ecommerce.orderproc.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order createOrder(OrderRequest request) {
        Order order = new Order();
        order.setCustomerEmail(request.getCustomerEmail());

        List<OrderItem> items = request.getItems().stream()
                .map(itemReq -> {
                    OrderItem item = new OrderItem();
                    item.setProductName(itemReq.getProductName());
                    item.setQuantity(itemReq.getQuantity());
                    item.setPrice(itemReq.getPrice());
                    item.setOrder(order);
                    return item;
                }).toList();

        order.setItems(items);
        return orderRepository.save(order);
    }

    public Order getOrderDetails(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + id));
    }

    public List<Order> listOrders(Optional<OrderStatus> status) {
        return status.map(orderRepository::findByStatus)
                .orElseGet(orderRepository::findAll);
    }

    @Transactional
    public Order updateOrderStatus(UUID id, OrderStatus status) {
        Order order = getOrderDetails(id);
        if(order.getStatus() == OrderStatus.CANCELLED) {
            throw new OrderConstraintException("Cannot update status of a CANCELLED order.");
        }
        else if(order.getStatus() == OrderStatus.DELIVERED) {
            throw new OrderConstraintException("Cannot update status of a DELIVERED order.");
        }else if(order.getStatus() == OrderStatus.PROCESSING && status == OrderStatus.PENDING) {
            throw new OrderConstraintException("Cannot revert status from PROCESSING to PENDING.");
        }else if(order.getStatus() == OrderStatus.SHIPPED && status == OrderStatus.PROCESSING) {
            throw new OrderConstraintException("Cannot revert status from SHIPPED to PROCESSING.");
        }
        order.setStatus(status);
        return orderRepository.save(order);
    }

    @Transactional
    public Order cancelOrder(UUID id) {
        Order order = getOrderDetails(id);

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new OrderConstraintException("Order can only be cancelled if status is PENDING. Current status: " + order.getStatus());
        }

        order.setStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    public static class OrderNotFoundException extends RuntimeException {
        public OrderNotFoundException(String message) { super(message); }
    }
    public static class OrderConstraintException extends RuntimeException {
        public OrderConstraintException(String message) { super(message); }
    }
}