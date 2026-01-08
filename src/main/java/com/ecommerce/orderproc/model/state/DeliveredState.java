package com.ecommerce.orderproc.model.state;

import com.ecommerce.orderproc.exception.InvalidStateTransitionException;
import com.ecommerce.orderproc.model.Order;
import com.ecommerce.orderproc.model.OrderStatus;

public class DeliveredState implements OrderState {
    @Override
    public void next(Order order) {
        throw new InvalidStateTransitionException("Order is already DELIVERED (Terminal State).");
    }

    @Override
    public void cancel(Order order) {
        throw new InvalidStateTransitionException("Cannot cancel a delivered order. Please initiate a refund.");
    }

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.DELIVERED;
    }
}