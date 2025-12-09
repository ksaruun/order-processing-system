package com.ecommerce.orderproc.model;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderRequest {
    private String customerEmail;
    private List<ItemRequest> items;

    @Data
    public static class ItemRequest {
        private String productName;
        private int quantity;
        private BigDecimal price;
    }
}