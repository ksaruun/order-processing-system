package com.ecommerce.orderproc.service;

import com.ecommerce.orderproc.exception.OrderNotFoundException;
import com.ecommerce.orderproc.model.*;
import com.ecommerce.orderproc.repository.OrderRepository;
import com.ecommerce.orderproc.service.producer.OrderEventProducer;
import com.ecommerce.orderproc.strategy.PaymentFactory;
import com.ecommerce.orderproc.strategy.PaymentStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final PaymentFactory paymentFactory;

    private final OrderEventProducer eventProducer;

    public OrderService(OrderRepository orderRepository, PaymentFactory paymentFactory, OrderEventProducer eventProducer) {
        this.orderRepository = orderRepository;
        this.paymentFactory = paymentFactory;
        this.eventProducer = eventProducer;
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
        Order savedOrder = orderRepository.save(order);
        // Broadcast Event
        log.info("Order Created: {}", savedOrder.getId());
        eventProducer.sendOrderEvent(savedOrder, "OrderCreated");
        return savedOrder;
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
    public Order moveToNextStatus(UUID id){
        Order order = getOrderDetails(id);
        order.nextState();
        Order saved = orderRepository.save(order);
        if(order.getStatus().equals(OrderStatus.PROCESSING)){
            log.info("Order Processed: {}", order.getId());
            eventProducer.sendOrderEvent(saved, "OrderCreated");
        }
        return saved;
    }

    @Transactional
    public Order cancelOrder(UUID id) {
        Order order = getOrderDetails(id);
        order.cancelOrder();
        return orderRepository.save(order);
    }

    @Transactional
    public void handlePayment(UUID orderId, String paymentMethod) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        PaymentStrategy strategy = paymentFactory.getStrategy(paymentMethod);
        boolean success = strategy.process(order.getTotalAmount());
        if (success) {
            order.nextState(); // Move from PENDING to PROCESSED via State Pattern
            orderRepository.save(order);
            log.info("Order Payment Done: {}", order.getId());
            eventProducer.sendOrderEvent(order, "PaymentDone");
        }
    }

    @Transactional
    public void shipOrder(UUID id) {
        Order order = orderRepository.findById(id).orElseThrow();
        order.nextState(); // PROCESSED -> SHIPPED
        orderRepository.save(order);
        log.info("Order Shipped {}", order.getId());
        //eventProducer.sendOrderEvent(order, "OrderShipped");
    }
}