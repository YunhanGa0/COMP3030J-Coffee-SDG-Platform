package com.coffee_backend.entity;

import com.coffee_backend.enumType.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

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
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;

    @Column(nullable = false)
    private Double totalAmount; // = pricePerBag × quantity

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PAID;

    @Column(name = "order_time", nullable = false)
    private LocalDateTime orderTime;

    @PrePersist
    protected void onCreate() {
        orderTime = LocalDateTime.now();
    }
}
