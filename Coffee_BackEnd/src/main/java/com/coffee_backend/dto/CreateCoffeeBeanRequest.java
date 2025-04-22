package com.coffee_backend.dto;
import lombok.Data;
/**
 * @author Le Liu
 * @create 2025-04
 */
@Data
public class CreateCoffeeBeanRequest {
    private String name;
    private String variety;
    private String processMethod;
    private String roastLevel;
    private String flavorNotes;
    private Double weightPerBagKg;
    private Integer bagStock;
    private Double pricePerBag;
    private Boolean available;
    private Boolean limitedEdition;
    private String imageUrl;
}
