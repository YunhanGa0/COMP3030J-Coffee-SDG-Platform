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
    private Boolean available;     // 是否上架销售
    private Boolean limitedEdition; // 是否为限量豆
    private String imageUrl;
}
