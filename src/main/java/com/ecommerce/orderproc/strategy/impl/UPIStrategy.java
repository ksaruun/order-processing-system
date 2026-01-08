package com.ecommerce.orderproc.strategy.impl;

import com.ecommerce.orderproc.strategy.PaymentStrategy;
import org.springframework.stereotype.Component;

@Component
public class UPIStrategy implements PaymentStrategy {
    @Override
    public boolean process(double amount) {
        System.out.println("Processing UPI payment of $" + amount);
        return true;
    }

    @Override
    public String getPaymentType() {
        return "UPI";
    }
}
