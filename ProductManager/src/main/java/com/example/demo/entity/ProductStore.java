package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "v_product_stores")
public class ProductStore {

    @Id
    @Column(name = "price_id")
    private Long priceId;

    @Column(name = "stock_id")
    private Long stockId;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "store_id", referencedColumnName = "id")
    private Store store;

    @Column(name = "sale_price")
    private BigDecimal salePrice;

    @Column(name = "stock_quantity")
    private Long stockQuantity;

    @Column(name = "price_created_at")
    private LocalDateTime priceCreatedAt;

    @Column(name = "stock_created_at")
    private LocalDateTime stockCreatedAt;

    @Column(name = "price_updated_at")
    private LocalDateTime priceUpdatedAt;

    @Column(name = "stock_updated_at")
    private LocalDateTime stockUpdatedAt;
}
