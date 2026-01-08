package com.ecommerce.orderproc.service.producer;

import com.ecommerce.orderproc.dto.OrderEvent;
import com.ecommerce.orderproc.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "order-events";

    public void sendOrderEvent(Order order, String eventType) {
        OrderEvent event = OrderEvent.builder()
                .orderId(order.getId())
                .status(order.getStatus())
                .eventType(eventType)
                .timestamp(LocalDateTime.now())
                .build();

        kafkaTemplate.send(TOPIC, order.getId().toString(), event);
    }
}
