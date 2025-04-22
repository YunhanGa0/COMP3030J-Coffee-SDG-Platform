package com.coffee_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;  // 课程标题

    @Column(columnDefinition = "TEXT")
    private String description; // 简要介绍

    private String category; // 类别（如：种植、处理、可持续发展）

    @Column(name = "cover_image_url")
    private String coverImageUrl; // 封面图 URL

    private Boolean published = true; // 是否对外可见
}
