package com.coffee_backend.service;

import cn.hutool.core.bean.BeanUtil;
import com.coffee_backend.dto.ApiResponse;
import com.coffee_backend.dto.CourseRequest;
import com.coffee_backend.entity.Course;
import com.coffee_backend.repo.CourseRepository;
import com.coffee_backend.repo.CourseVideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseVideoRepository courseVideoRepository;


    public ApiResponse createCourse(CourseRequest request) {
        // Check if title is null or empty
        if (request.getTitle() == null || request.getTitle().isEmpty()) {
            return ApiResponse.error(400, "Title cannot be null or empty");
        }

        // Check if description is null or empty
        if (request.getDescription() == null || request.getDescription().isEmpty()) {
            return ApiResponse.error(400, "Description cannot be null or empty");
        }

        // Check if category is null or empty
        if (request.getCategory() == null || request.getCategory().isEmpty()) {
            return ApiResponse.error(400, "Category cannot be null or empty");
        }

        // Check if coverImageUrl is null (optional, if you allow null)
        if (request.getCoverImageUrl() == null) {
            return ApiResponse.error(400, "Cover image URL cannot be null");
        }

        Course course = BeanUtil.copyProperties(request, Course.class);
        Course saved = courseRepository.save(course);

        return ApiResponse.success();
    }

    public ApiResponse queryCourse(Long id) {
        Optional<Course> optional = courseRepository.findById(id);
        if (optional.isEmpty()){
            return ApiResponse.error(404, "Course not found");
        }

        return ApiResponse.success(optional.get());
    }

    public ApiResponse queryAllCourse() {
        return ApiResponse.success(courseRepository.findAll());
    }

    @Transactional
    public ApiResponse deleteCourse(Long id) {
        Optional<Course> optional = courseRepository.findById(id);
        if (optional.isEmpty()){
            return ApiResponse.error(404, "Course not found");
        }

        courseVideoRepository.deleteByCourseId(id);

        courseRepository.deleteById(id);

        return ApiResponse.success();
    }
}
