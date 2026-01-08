package com.ecommerce.orderproc.model.state;

import com.ecommerce.orderproc.model.Order;
import com.ecommerce.orderproc.model.OrderStatus;
import com.ecommerce.orderproc.exception.InvalidStateTransitionException;

public class CancelledState implements OrderState {

    @Override
    public void next(Order order) {
        throw new InvalidStateTransitionException("Cannot move to next state: Order is already CANCELLED.");
    }

    @Override
    public void cancel(Order order) {
        // Optional: Could do nothing, or throw an exception to warn the UI
        throw new InvalidStateTransitionException("Order is already in CANCELLED state.");
    }

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.CANCELLED;
    }
}