package com.coffee_backend.dto;


import lombok.Data;

@Data
public class CreateOrderRequest {

    private Long coffeeBeanId;
    private Integer quantity;
    private String shippingAddress;
    private String contactNumber;
    private String recipientName;
}
