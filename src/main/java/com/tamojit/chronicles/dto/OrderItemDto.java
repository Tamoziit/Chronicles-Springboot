package com.tamojit.chronicles.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {
    private long productId;
    private String productName;
    private String productBrand;
    private int quantity;
    private BigDecimal price;
}
