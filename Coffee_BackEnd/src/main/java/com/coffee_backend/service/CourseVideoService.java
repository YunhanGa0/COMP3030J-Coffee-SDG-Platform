package com.coffee_backend.service;

import cn.hutool.core.bean.BeanUtil;
import com.coffee_backend.dto.ApiResponse;
import com.coffee_backend.dto.CourseVideoRequest;
import com.coffee_backend.entity.Course;
import com.coffee_backend.entity.CourseVideo;
import com.coffee_backend.repo.CourseRepository;
import com.coffee_backend.repo.CourseVideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CourseVideoService {
    @Autowired
    private CourseVideoRepository courseVideoRepository;

    @Autowired
    private CourseRepository courseRepository;

    public ApiResponse createCourseVideo(Long courseId, CourseVideoRequest request) {
        // Check if title is null or empty
        if (request.getTitle() == null || request.getTitle().isEmpty()) {
            return ApiResponse.error(400, "Title cannot be null or empty");
        }

        // Check if description is null or empty
        if (request.getDescription() == null || request.getDescription().isEmpty()) {
            return ApiResponse.error(400, "Description cannot be null or empty");
        }

        // Check if videoUrl is null or empty (optional, you can adjust this if the video URL is not mandatory)
        if (request.getVideoUrl() == null || request.getVideoUrl().isEmpty()) {
            return ApiResponse.error(400, "Video URL cannot be null or empty");
        }

        Optional<Course> optional = courseRepository.findById(courseId);
        if (optional.isEmpty()){
            return ApiResponse.error(404, "Course not found");
        }

        CourseVideo courseVideo = BeanUtil.copyProperties(request, CourseVideo.class);

        courseVideo.setCourse(optional.get());

        courseVideoRepository.save(courseVideo);

        return ApiResponse.success();
    }

    public ApiResponse queryCourseVideo(Long courseId) {
        Optional<CourseVideo> optional = courseVideoRepository.findById(courseId);
        if (optional.isEmpty()){
            return ApiResponse.error(404, "Course video not found");
        }

        return ApiResponse.success(optional.get());
    }

    public ApiResponse deleteCourseVideo(Long courseId) {
        Optional<CourseVideo> optional = courseVideoRepository.findById(courseId);
        if (optional.isEmpty()){
            return ApiResponse.error(404, "Course video not found");
        }

        courseVideoRepository.deleteById(courseId);

        return ApiResponse.success();
    }
}
