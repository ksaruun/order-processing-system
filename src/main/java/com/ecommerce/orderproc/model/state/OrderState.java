package com.ecommerce.orderproc.model.state;

import com.ecommerce.orderproc.model.Order;
import com.ecommerce.orderproc.model.OrderStatus;

public interface OrderState {
    void next(Order order);    // Move to the next logical stage
    void cancel(Order order);  // Attempt to cancel
    OrderStatus getStatus();   // Get the current Enum status
}