package com.tamojit.chronicles.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;

    // Relationships
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category; // m prods. : 1 Category

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    // cascade delete all associated images on deleting the prod.
    private List<Image> images; // 1 prod : n images
}
