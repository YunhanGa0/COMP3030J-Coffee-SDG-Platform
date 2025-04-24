package com.coffee_backend.dto;

import lombok.Data;

@Data
public class CoffeeBeanDetailResponse {
    private Long id;
    private String name;
    private String variety;
    private String processMethod;
    private String roastLevel;
    private String flavorNotes;
    private Double weightPerBagKg;
    private Integer bagStock;
    private Double pricePerBag;
    private String imageUrl;
}
