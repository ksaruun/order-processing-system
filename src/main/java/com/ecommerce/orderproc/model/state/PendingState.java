package com.ecommerce.orderproc.model.state;

import com.ecommerce.orderproc.model.Order;
import com.ecommerce.orderproc.model.OrderStatus;

public class PendingState implements OrderState {
    @Override
    public void next(Order order) {
        order.setState(new ProcessingState()); // Transition
    }

    @Override
    public void cancel(Order order) {
        order.setState(new CancelledState()); // Allowed
    }

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.PENDING;
    }
}