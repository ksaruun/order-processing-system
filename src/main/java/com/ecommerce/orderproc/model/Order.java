package com.ecommerce.orderproc.model;

import com.ecommerce.orderproc.model.state.*;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "customer_order")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String customerEmail;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

    @Transient // This field isn't saved to the DB, it's for logic only
    private OrderState state = new PendingState();

    @PostLoad
    private void setInitialState() {
        switch (this.status) {
            case PENDING -> this.state = new PendingState();
            case PROCESSING -> this.state = new ProcessingState();
            case SHIPPED -> this.state = new ShippedState();
            case DELIVERED -> this.state = new DeliveredState();
            case CANCELLED -> this.state = new CancelledState();
        }
    }

    // The methods now just "delegate" to the state object
    public void nextState() {
        state.next(this);
        this.status = state.getStatus(); // Keep DB field in sync
    }

    public void cancelOrder() {
        state.cancel(this);
        this.status = state.getStatus();
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public double getTotalAmount(){
        //get total amount from items
        double totalAmount = this.items.stream()
                .mapToDouble(OrderItem::getPrice)
                .sum();
        return totalAmount;
    }
}