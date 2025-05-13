package com.coffee_backend.controller;

import com.coffee_backend.dto.*;
import com.coffee_backend.service.FarmBlogService;
import com.coffee_backend.service.FarmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Farm Controller
 * Handles endpoints related to farm operations and farm blogs
 */
@RestController
@RequestMapping("/api/farms")
@Tag(name = "Farm Management", description = "APIs for managing farms and farm blogs")
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
    @Operation(summary = "List farms", description = "Retrieves a paginated list of farms")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Farms retrieved successfully")
    })
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
    @Operation(summary = "Get farm details", description = "Retrieves detailed information about a specific farm")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Farm details retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Farm not found")
    })
    @GetMapping("/{id}")
    public ApiResponse getById(@Parameter(description = "ID of the farm") @PathVariable Long id){
        return farmService.getById(id);
    }

    /**
     * Save farm profile information (farmer only)
     * 
     * @param saveProfileRequest Profile data
     * @return Operation result
     */
    @Operation(summary = "Create farm profile", description = "Creates a farm profile. Only accessible by farmers.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Farm profile created successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Farmer privileges required")
    })
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
    @Operation(summary = "Update farm profile", description = "Updates a farm profile. Only accessible by farmers who own the farm.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Farm profile updated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Farmer privileges required or not your farm"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Farm profile not found")
    })
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
    @Operation(summary = "Get farm status", description = "Retrieves status information for the current farmer's farm. Only accessible by farmers.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Farm status retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Farmer privileges required")
    })
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
    @Operation(summary = "Get own farm profile", description = "Retrieves the profile of the current farmer's farm. Only accessible by farmers.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Farm profile retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Farmer privileges required"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Farm profile not found")
    })
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
    @Operation(summary = "Create farm blog", description = "Creates a new blog for a specific farm")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Blog created successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Farm not found")
    })
    @PostMapping("/{farmid}/blogs")
    public ApiResponse createBlog(
        @Parameter(description = "ID of the farm") @PathVariable Long farmid, 
        @RequestBody FarmBlogRequest request){
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
    @Operation(summary = "Update farm blog", description = "Updates an existing blog for a specific farm")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Blog updated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Farm or blog not found")
    })
    @PutMapping("/{farmid}/blogs/{blogid}")
    public ApiResponse updateBlog(
        @Parameter(description = "ID of the farm") @PathVariable Long farmid, 
        @RequestBody FarmBlogRequest request, 
        @Parameter(description = "ID of the blog to update") @PathVariable Long blogid){
        return farmBlogService.updateBlog(farmid, request, blogid);
    }

    /**
     * Delete a blog
     * 
     * @param farmid Farm ID
     * @param blogid Blog ID to delete
     * @return Operation result
     */
    @Operation(summary = "Delete farm blog", description = "Deletes a blog from a specific farm")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Blog deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Farm or blog not found")
    })
    @DeleteMapping("/{farmid}/blogs/{blogid}")
    public ApiResponse deleteBlog(
        @Parameter(description = "ID of the farm") @PathVariable Long farmid, 
        @Parameter(description = "ID of the blog to delete") @PathVariable Long blogid){
        return farmBlogService.deleteBlog(farmid, blogid);
    }

    /**
     * Get list of blogs for a farm with pagination
     * 
     * @param farmid Farm ID
     * @param request Pagination parameters
     * @return List of blogs
     */
    @Operation(summary = "List farm blogs", description = "Retrieves a paginated list of blogs for a specific farm")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Blogs retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Farm not found")
    })
    @GetMapping("/{farmid}/blogs")
    public ApiResponse listBlog(
        @Parameter(description = "ID of the farm") @PathVariable Long farmid, 
        @ModelAttribute PageQueryRequest request){
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
    @Operation(summary = "Update blog publication status", description = "Updates the publication status of a specific blog")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Blog status updated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Farm or blog not found")
    })
    @PatchMapping("/{farmid}/blogs/{blogid}/publish")
    public ApiResponse patchBlog(
        @Parameter(description = "ID of the farm") @PathVariable Long farmid, 
        @RequestBody BlogPatchRequest request, 
        @Parameter(description = "ID of the blog to update") @PathVariable Long blogid){
        return farmBlogService.patchBlog(farmid, request, blogid);
    }

    /**
     * Get details of a specific blog
     * 
     * @param farmid Farm ID
     * @param blogid Blog ID
     * @return Blog details
     */
    @Operation(summary = "Get blog details", description = "Retrieves detailed information about a specific blog")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Blog details retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Farm or blog not found")
    })
    @GetMapping("/{farmid}/blogs/{blogid}")
    public ApiResponse getBlog(
        @Parameter(description = "ID of the farm") @PathVariable Long farmid, 
        @Parameter(description = "ID of the blog") @PathVariable Long blogid){
        return farmBlogService.getBlog(farmid, blogid);
    }
}
