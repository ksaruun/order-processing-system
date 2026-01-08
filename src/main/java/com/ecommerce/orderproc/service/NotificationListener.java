package com.ecommerce.orderproc.service;

import com.ecommerce.orderproc.dto.OrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationListener {

    @KafkaListener(topics = "order-events", groupId = "notification-group")
    public void handleOrderEvents(OrderEvent event) {
        switch (event.getEventType()) {
            case "OrderCreated" -> sendEmail(event, " has been received!");
            case "OrderShipped" -> sendEmail(event, " is on the way!");
            case "PaymentDone" -> sendEmail(event, " has done payment successful!");
        }
    }

    private void sendEmail(OrderEvent event, String s) {
        log.info("Order with id : " + event.getOrderId() + " " + s);
    }
}