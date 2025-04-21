package com.coffee_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "coffee_bean_id", nullable = false)
    private CoffeeBean coffeeBean;

    @Column(nullable = false)
    private Integer quantity; // 购买袋数

    @Column(name = "price_per_bag", nullable = false)
    private Double pricePerBag;

    @Column(nullable = false)
    private Double subtotal; // quantity × pricePerBag
}
