package com.coffee_backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FarmFullResponse {
    private Long id;
    private UserDTO user;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
