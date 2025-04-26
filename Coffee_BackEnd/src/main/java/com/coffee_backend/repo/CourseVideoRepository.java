package com.coffee_backend.repo;

import com.coffee_backend.entity.CourseVideo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseVideoRepository extends JpaRepository<CourseVideo, Long> {
    void deleteByCourseId(Long courseId);
}
