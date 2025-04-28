// src/main/java/com/coffee_backend/dto/CreateOrderRequest.java
package com.coffee_backend.dto;


import lombok.Data;

@Data
public class CreateOrderRequest {

    private Long coffeeBeanId;
    private Integer quantity;
}
