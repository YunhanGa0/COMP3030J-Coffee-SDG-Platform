package com.coffee_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "course_videos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseVideo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;  // 所属课程

    @Column(nullable = false)
    private String title; // 视频标题

    @Column(columnDefinition = "TEXT")
    private String description; // 简要说明

    @Column(name = "video_url", nullable = false)
    private String videoUrl; // 视频嵌入链接（如 YouTube / Bilibili）



    private Boolean published = true; // 是否显示
}
