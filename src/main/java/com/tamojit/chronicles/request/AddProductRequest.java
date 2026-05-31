package com.tamojit.chronicles.request;

import lombok.Data;

import java.math.BigDecimal;

@Data // Data Object of mother 'Product' DB Entity
public class AddProductRequest {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private String category;
}
