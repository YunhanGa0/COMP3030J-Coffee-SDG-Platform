package com.coffee_backend.controller;

import com.coffee_backend.dto.ApiResponse;
import com.coffee_backend.dto.CourseRequest;
import com.coffee_backend.dto.CourseVideoRequest;
import com.coffee_backend.service.CourseService;
import com.coffee_backend.service.CourseVideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing training courses and their associated videos.
 * Provides endpoints for CRUD operations on courses and course videos.
 */
@RestController
@RequestMapping("/api/courses")
@Tag(name = "Course Management", description = "APIs for managing training courses and videos")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseVideoService courseVideoService;

    /**
     * Creates a new training course (Admin only)
     * @param request Course creation request containing course details
     * @return ApiResponse with the result of the operation
     */
    @Operation(summary = "Create a new training course", description = "Creates a new training course. Only accessible by administrators.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Course created successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Admin privileges required")
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ApiResponse createCourse(@RequestBody CourseRequest request){
        return courseService.createCourse(request);
    }

    /**
     * Deletes a training course (Admin only)
     * @param id ID of the course to delete
     * @return ApiResponse with the result of the operation
     */
    @Operation(summary = "Delete a training course", description = "Deletes an existing training course. Only accessible by administrators.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Course deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Course not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Admin privileges required")
    })
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse deleteCourse(@Parameter(description = "ID of the course to delete") @PathVariable Long id){
        return courseService.deleteCourse(id);
    }

    /**
     * Publishes or unpublishes a training course (Admin only)
     * @param id ID of the course to publish/unpublish
     * @return ApiResponse with the result of the operation
     */
    @Operation(summary = "Publish/Unpublish a training course", description = "Changes the publication status of a training course. Only accessible by administrators.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Course publication status updated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Course not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Admin privileges required")
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("published/{id}")
    public ApiResponse publishCourse(@Parameter(description = "ID of the course to publish/unpublish") @PathVariable Long id){
        return courseService.publishCourse(id);
    }

    /**
     * Updates an existing training course (Admin only)
     * @param id ID of the course to update
     * @param request Updated course information
     * @return ApiResponse with the result of the operation
     */
    @Operation(summary = "Update a training course", description = "Updates an existing training course. Only accessible by administrators.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Course updated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Course not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Admin privileges required")
    })
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/update/{id}")
    public ApiResponse updateCourse(
        @Parameter(description = "ID of the course to update") @PathVariable Long id,
        @RequestBody CourseRequest request){
        return courseService.updateCourse(id, request);
    }

    /**
     * Retrieves a specific training course
     * @param id ID of the course to retrieve
     * @return ApiResponse containing the course information
     */
    @Operation(summary = "Get a specific course", description = "Retrieves detailed information about a specific training course.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Course retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Course not found")
    })
    @GetMapping("/{id}")
    public ApiResponse queryCourse(@Parameter(description = "ID of the course to retrieve") @PathVariable Long id){
        return courseService.queryCourse(id);
    }

    /**
     * Retrieves all training courses
     * @return ApiResponse containing a list of all courses
     */
    @Operation(summary = "Get all courses", description = "Retrieves a list of all available training courses.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Courses retrieved successfully")
    })
    @GetMapping
    public ApiResponse queryAllCourse(){
        return courseService.queryAllCourse();
    }

    /**
     * Creates a new video for a training course (Admin only)
     * @param courseId ID of the course to add the video to
     * @param request Video creation request containing video details
     * @return ApiResponse with the result of the operation
     */
    @Operation(summary = "Add video to a course", description = "Adds a new video to an existing training course. Only accessible by administrators.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Video added successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Course not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Admin privileges required")
    })
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/{courseId}/videos")
    public ApiResponse createCourseVideo(
        @Parameter(description = "ID of the course to add the video to") @PathVariable Long courseId,
        @RequestBody CourseVideoRequest request){
        return courseVideoService.createCourseVideo(courseId, request);
    }

    /**
     * Retrieves all videos for a specific course
     * @param courseId ID of the course to get videos for
     * @return ApiResponse containing the list of course videos
     */
    @Operation(summary = "Get course videos", description = "Retrieves all videos associated with a specific training course.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Videos retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Course not found")
    })
    @GetMapping("/{courseId}/videos")
    public ApiResponse queryCourseVideo(@Parameter(description = "ID of the course to get videos for") @PathVariable Long courseId){
        return courseVideoService.queryCourseVideo(courseId);
    }

    /**
     * Deletes all videos for a specific course (Admin only)
     * @param courseId ID of the course to delete videos from
     * @return ApiResponse with the result of the operation
     */
    @Operation(summary = "Delete course videos", description = "Deletes all videos associated with a specific training course. Only accessible by administrators.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Videos deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Course not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Admin privileges required")
    })
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/{courseId}/videos")
    public ApiResponse deleteCourseVideo(@Parameter(description = "ID of the course to delete videos from") @PathVariable Long courseId){
        return courseVideoService.deleteCourseVideo(courseId);
    }
}
