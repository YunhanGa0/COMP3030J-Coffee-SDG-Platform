package com.coffee_backend.controller;

import com.coffee_backend.dto.*;
import com.coffee_backend.service.FarmBlogService;
import com.coffee_backend.service.FarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Farm Controller
 * Handles endpoints related to farm operations and farm blogs
 */
@RestController
@RequestMapping("/api/farms")
public class FarmController {

    @Autowired
    private FarmService farmService;

    @Autowired
    private FarmBlogService farmBlogService;

    /**
     * Get list of farms with pagination
     * 
     * @param pageQueryRequest Pagination parameters
     * @return List of farms
     */
    @GetMapping
    public ApiResponse list(@ModelAttribute PageQueryRequest pageQueryRequest){
        return ApiResponse.success(farmService.list(pageQueryRequest));
    }

    /**
     * Get farm details by ID
     * 
     * @param id Farm ID
     * @return Farm details
     */
    @GetMapping("/{id}")
    public ApiResponse getById(@PathVariable Long id){
        return farmService.getById(id);
    }

    /**
     * Save farm profile information (farmer only)
     * 
     * @param saveProfileRequest Profile data
     * @return Operation result
     */
    @PreAuthorize("hasAuthority('FARMER')")
    @PostMapping("/profile")
    public ApiResponse saveProfile(@RequestBody SaveProfileRequest saveProfileRequest){
        return farmService.saveProfile(saveProfileRequest);
    }

    /**
     * Update farm profile information (farmer only)
     * 
     * @param saveProfileRequest Updated profile data
     * @return Operation result
     */
    @PreAuthorize("hasAuthority('FARMER')")
    @PutMapping("/update")
    public ApiResponse updateProfile(@RequestBody SaveProfileRequest saveProfileRequest){
        return farmService.updateProfile(saveProfileRequest);
    }

    /**
     * Query farm status (farmer only)
     * 
     * @return Farm status information
     */
    @PreAuthorize("hasAuthority('FARMER')")
    @GetMapping("/status")
    public ApiResponse queryStatus(){
        return farmService.queryStatus();
    }

    /**
     * Get farm profile (farmer only)
     * 
     * @return Farm profile information
     */
    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('FARMER')")
    public ApiResponse getProfile() {
        return farmService.getProfile();
    }

    // Farm Blog related endpoints

    /**
     * Create a new blog for a farm
     * 
     * @param farmid Farm ID
     * @param request Blog content data
     * @return Operation result
     */
    @PostMapping("/{farmid}/blogs")
    public ApiResponse createBlog(@PathVariable Long farmid, @RequestBody FarmBlogRequest request){
        return farmBlogService.createBlog(farmid, request);
    }

    /**
     * Update an existing blog
     * 
     * @param farmid Farm ID
     * @param request Updated blog content
     * @param blogid Blog ID to update
     * @return Operation result
     */
    @PutMapping("/{farmid}/blogs/{blogid}")
    public ApiResponse updateBlog(@PathVariable Long farmid, @RequestBody FarmBlogRequest request, @PathVariable Long blogid){
        return farmBlogService.updateBlog(farmid, request, blogid);
    }

    /**
     * Delete a blog
     * 
     * @param farmid Farm ID
     * @param blogid Blog ID to delete
     * @return Operation result
     */
    @DeleteMapping("/{farmid}/blogs/{blogid}")
    public ApiResponse deleteBlog(@PathVariable Long farmid, @PathVariable Long blogid){
        return farmBlogService.deleteBlog(farmid, blogid);
    }

    /**
     * Get list of blogs for a farm with pagination
     * 
     * @param farmid Farm ID
     * @param request Pagination parameters
     * @return List of blogs
     */
    @GetMapping("/{farmid}/blogs")
    public ApiResponse listBlog(@PathVariable Long farmid, @ModelAttribute PageQueryRequest request){
        return farmBlogService.listBlog(farmid, request);
    }

    /**
     * Update blog publication status
     * 
     * @param farmid Farm ID
     * @param request Patch data (publish status)
     * @param blogid Blog ID to update
     * @return Operation result
     */
    @PatchMapping("/{farmid}/blogs/{blogid}/publish")
    public ApiResponse patchBlog(@PathVariable Long farmid, @RequestBody BlogPatchRequest request, @PathVariable Long blogid){
        return farmBlogService.patchBlog(farmid, request, blogid);
    }

    /**
     * Get details of a specific blog
     * 
     * @param farmid Farm ID
     * @param blogid Blog ID
     * @return Blog details
     */
    @GetMapping("/{farmid}/blogs/{blogid}")
    public ApiResponse getBlog(@PathVariable Long farmid, @PathVariable Long blogid){
        return farmBlogService.getBlog(farmid, blogid);
    }

}
