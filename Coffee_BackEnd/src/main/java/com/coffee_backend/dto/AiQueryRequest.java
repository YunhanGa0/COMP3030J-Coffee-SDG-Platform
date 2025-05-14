package com.coffee_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiQueryRequest {
    // blogs or farms
    private String resource;
    // list or single
    private String mode;
    private Long targetId;
    // list options
    private Map<String, Object> filters;
    private String query;
}
