package com.coffee_backend.entity;

import com.coffee_backend.enumType.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "coffee_bean_id", nullable = false)
    private CoffeeBean coffeeBean;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double totalAmount; // = pricePerBag × quantity

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PAID;

    @Column(name = "order_time", nullable = false)
    private LocalDateTime orderTime;


    @Column(nullable = false, length = 255)
    private String shippingAddress;

    @Column(nullable = false, length = 50)
    private String contactNumber;

    @Column(length = 100)
    private String recipientName;

    @PrePersist
    protected void onCreate() {
        this.orderTime = LocalDateTime.now();
        this.totalAmount = this.coffeeBean.getPricePerBag() * this.quantity;
    }
}
