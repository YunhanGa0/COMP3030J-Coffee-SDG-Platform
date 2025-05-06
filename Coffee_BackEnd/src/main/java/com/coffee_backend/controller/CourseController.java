package com.coffee_backend.controller;

import com.coffee_backend.dto.ApiResponse;
import com.coffee_backend.dto.CourseRequest;
import com.coffee_backend.dto.CourseVideoRequest;
import com.coffee_backend.service.CourseService;
import com.coffee_backend.service.CourseVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseVideoService courseVideoService;

    // 管理员创建培训课程
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ApiResponse createCourse(@RequestBody CourseRequest request){
        return courseService.createCourse(request);
    }

    /**
     * 管理员删除培训课程
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse deleteCourse(@PathVariable Long id){
        return courseService.deleteCourse(id);
    }

    /**
     * 管理员发布/取消发布培训课程
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("published/{id}")
    public ApiResponse publishCourse(@PathVariable Long id){
        return courseService.publishCourse(id);
    }

    /**
     * 管理员修改培训课程
     * @param id
     * @param request
     * @return
     */
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/update/{id}")
    public ApiResponse updateCourse(@PathVariable Long id, @RequestBody CourseRequest request){
        return courseService.updateCourse(id, request);
    }

    @GetMapping("/{id}")
    public ApiResponse queryCourse(@PathVariable Long id){
        return courseService.queryCourse(id);
    }

    @GetMapping
    public ApiResponse queryAllCourse(){
        return courseService.queryAllCourse();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/{courseId}/videos")
    public ApiResponse createCourseVideo(@PathVariable Long courseId, @RequestBody CourseVideoRequest request){
        return courseVideoService.createCourseVideo(courseId, request);
    }

    @GetMapping("/{courseId}/videos")
    public ApiResponse queryCourseVideo(@PathVariable Long courseId){
        return courseVideoService.queryCourseVideo(courseId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/{courseId}/videos")
    public ApiResponse deleteCourseVideo(@PathVariable Long courseId){
        return courseVideoService.deleteCourseVideo(courseId);
    }
}
