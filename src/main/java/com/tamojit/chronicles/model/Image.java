package com.tamojit.chronicles.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String fileType;

    @Lob
    // Specifies that the annotated persistent property or field should be persisted as a large object to a database-native large object (LOB) type.
    private Blob image;
    private String downloadUrl;

    // Relationship defn.
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
