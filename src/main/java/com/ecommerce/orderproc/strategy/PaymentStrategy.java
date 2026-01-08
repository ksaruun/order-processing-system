package com.ecommerce.orderproc.strategy;

import com.ecommerce.orderproc.model.Order;

public interface PaymentStrategy {
    boolean process(double amount);
    String getPaymentType(); // Used to identify which strategy to use
}