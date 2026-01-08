package com.ecommerce.orderproc.controller;

import com.ecommerce.orderproc.model.Order;
import com.ecommerce.orderproc.model.OrderStatus;
import com.ecommerce.orderproc.model.OrderRequest;
import com.ecommerce.orderproc.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest request) {
        Order newOrder = orderService.createOrder(request);
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.getOrderDetails(id));
    }

    @GetMapping
    public ResponseEntity<List<Order>> listOrders(@RequestParam(required = false) OrderStatus status) {
        return ResponseEntity.ok(orderService.listOrders(Optional.ofNullable(status)));
    }

    @PatchMapping("/{id}/next")
    public ResponseEntity<Order> advanceOrder(@PathVariable UUID id) {
        Order next = orderService.moveToNextStatus(id);
        return ResponseEntity.ok(next);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable UUID id) {
        Order cancelledOrder = orderService.cancelOrder(id);
        return ResponseEntity.ok(cancelledOrder);
    }

}