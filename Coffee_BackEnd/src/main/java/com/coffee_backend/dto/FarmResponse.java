package com.coffee_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FarmResponse {
    private Long id;
    private Long userId;
    private String farmName;
    private String country;
    private String location;
    private Double size;
    private Integer establishedYear;
    private String description;
    private String imageUrl;
    private Integer elevation;
    private String soilType;
    private Boolean isCertificated;
}
