package com.ecommerce.orderproc.model.state;

import com.ecommerce.orderproc.model.Order;
import com.ecommerce.orderproc.model.OrderStatus;

public class ProcessingState implements OrderState {
    @Override
    public void next(Order order) {
        order.setState(new ShippedState());
    }

    @Override
    public void cancel(Order order) {
        throw new IllegalStateException("Cannot cancel an order already in processing!");
    }

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.PROCESSING;
    }
}