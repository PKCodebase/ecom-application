package com.app.ecom_application.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemRequest {
    private Long productId;
    private Integer quantity;
}
