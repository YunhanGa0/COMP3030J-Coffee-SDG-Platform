package com.coffee_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseVideoRequest {
    private String title;
    private String description;
    private String videoUrl;
    private Boolean published;
}
