package com.coffee_backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemResponse {

    private Long id;              // 行项 id
    private Long coffeeBeanId;    // 咖啡豆 id
    private String coffeeBeanName;// 名称（如需要）
    private Integer quantity;     // 购买袋数
    private Double pricePerBag;   // 单价
    private Double subtotal;      // 小计
}

