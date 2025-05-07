package com.coffee_backend.dto;


import com.coffee_backend.enumType.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private Double totalAmount;
    private Long coffeeBeanId;
    private Integer quantity;
    private String coffeeBeanName;
    private String coffeeBeanImageUrl;
    private OrderStatus status;
    private String shippingAddress;
    private String contactNumber;
    private String recipientName;
    private LocalDateTime orderTime;
}
