package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "m_products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @Column(name = "manufacturer_id")
    private Long manufacturerId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "image")
    private String image;

    @Column(name = "cost_price")
    private Long costPrice;

    @Column(name = "retail_price")
    private Long retailPrice;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "product")
    private List<ProductStore> productStores;
}
