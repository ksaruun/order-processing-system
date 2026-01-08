package com.ecommerce.orderproc.strategy;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PaymentFactory {

    private final Map<String, PaymentStrategy> strategies;

    // Spring automatically fills this map with all PaymentStrategy beans
    // Key = The bean name or a custom ID, Value = The implementation
    public PaymentFactory(List<PaymentStrategy> strategyList) {
        strategies = strategyList.stream()
                .collect(Collectors.toMap(PaymentStrategy::getPaymentType, s -> s));
    }

    public PaymentStrategy getStrategy(String type) {
        PaymentStrategy strategy = strategies.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("Invalid payment type: " + type);
        }
        return strategy;
    }
}