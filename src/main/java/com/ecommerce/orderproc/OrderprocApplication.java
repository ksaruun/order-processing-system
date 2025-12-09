package com.ecommerce.orderproc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // Enables the @Scheduled annotation used in OrderScheduler
public class OrderprocApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderprocApplication.class, args);
    }
}