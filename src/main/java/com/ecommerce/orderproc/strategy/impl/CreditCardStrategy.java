package com.ecommerce.orderproc.strategy.impl;

import com.ecommerce.orderproc.strategy.PaymentStrategy;
import org.springframework.stereotype.Component;

@Component
public class CreditCardStrategy implements PaymentStrategy {
    @Override
    public boolean process(double amount) {
        System.out.println("Processing Credit Card payment of $" + amount);
        return true; // Logic for Stripe/Square would go here
    }

    @Override
    public String getPaymentType() { return "CREDIT_CARD"; }
}