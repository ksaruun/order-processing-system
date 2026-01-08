package com.ecommerce.orderproc.dto;

import com.ecommerce.orderproc.model.OrderStatus;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {
    private String eventId;    // Unique ID for tracking/deduplication
    private UUID orderId;
    private OrderStatus status;
    private String eventType;  // e.g., "OrderCreated", "PaymentDone"
    private LocalDateTime timestamp;
    private String customerEmail;
}