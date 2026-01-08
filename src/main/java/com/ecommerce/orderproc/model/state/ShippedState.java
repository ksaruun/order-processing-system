package com.ecommerce.orderproc.model.state;

import com.ecommerce.orderproc.model.Order;
import com.ecommerce.orderproc.model.OrderStatus;
import com.ecommerce.orderproc.exception.InvalidStateTransitionException;

public class ShippedState implements OrderState {

    @Override
    public void next(Order order) {
        // Move to the final successful state
        order.setState(new DeliveredState());
    }

    @Override
    public void cancel(Order order) {
        // Business Rule: Once shipped, the customer must initiate a "Return" instead of "Cancel"
        throw new InvalidStateTransitionException("Cannot cancel an order that has already been shipped.");
    }

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.SHIPPED;
    }
}