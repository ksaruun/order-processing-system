package com.ecommerce.orderproc.service.consumer;

import com.ecommerce.orderproc.dto.OrderEvent;
import com.ecommerce.orderproc.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ShippingConsumer {

    private final OrderService orderService;

    public ShippingConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(topics = "order-events", groupId = "shipping-group")
    public void handleOrderEvent(OrderEvent event) throws InterruptedException {
        if(event.getEventType().equals("PaymentDone")) {
            log.info("Processing order for shipping: {}", event.getOrderId());
            Thread.sleep(60000);
            orderService.shipOrder(event.getOrderId());
        }
    }
}
