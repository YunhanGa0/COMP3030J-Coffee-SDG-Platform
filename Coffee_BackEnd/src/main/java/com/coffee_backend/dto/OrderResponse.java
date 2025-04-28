package com.coffee_backend.dto;


import com.coffee_backend.enumType.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private Double totalAmount;
    private OrderStatus status;
    private LocalDateTime orderTime;
}
