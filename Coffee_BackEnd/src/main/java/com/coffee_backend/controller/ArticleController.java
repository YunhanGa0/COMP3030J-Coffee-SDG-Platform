package com.coffee_backend.controller;


import com.coffee_backend.dto.ApiResponse;
import com.coffee_backend.dto.ArticleRequest;

import com.coffee_backend.service.AliyunOssImageStorageService;
import com.coffee_backend.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



/**
 * Article Controller
 * Handles operations related to articles including CRUD operations
 * and image upload functionality
 */
@RestController
@RequestMapping("/api/articles")
@Tag(name = "Articles", description = "APIs for managing articles and article images")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private AliyunOssImageStorageService imageStorageService;

    // Article management endpoints

    /**
     * Get list of all articles
     * 
     * @return List of articles
     */
    @Operation(summary = "List all articles", description = "Retrieves a list of all articles")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Articles retrieved successfully")
    })
    @GetMapping
    public ApiResponse list(){
        return articleService.list();
    }

    /**
     * Get article by ID
     * 
     * @param id Article ID
     * @return Article details
     */
    @Operation(summary = "Get article details", description = "Retrieves detailed information about a specific article")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Article retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Article not found")
    })
    @GetMapping("/{id}")
    public ApiResponse findById(@Parameter(description = "ID of the article") @PathVariable Long id){
        return articleService.findById(id);
    }

    /**
     * Create a new article (admin only)
     * 
     * @param articleRequest Article data
     * @return Operation result
     */
    @Operation(summary = "Create new article", description = "Creates a new article. Only accessible by administrators.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Article created successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Admin privileges required")
    })
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse save(@RequestBody ArticleRequest articleRequest){
        return articleService.save(articleRequest);
    }

    /**
     * Update an existing article (admin only)
     * 
     * @param id Article ID to update
     * @param articleRequest Updated article data
     * @return Operation result
     */
    @Operation(summary = "Update article", description = "Updates an existing article. Only accessible by administrators.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Article updated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Admin privileges required"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Article not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse update(@Parameter(description = "ID of the article to update") @PathVariable Long id, @RequestBody ArticleRequest articleRequest){
        return articleService.update(id, articleRequest);
    }

    /**
     * Delete an article (admin only)
     * 
     * @param id Article ID to delete
     * @return Operation result
     */
    @Operation(summary = "Delete article", description = "Deletes an article. Only accessible by administrators.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Article deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Admin privileges required"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Article not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse delete(@Parameter(description = "ID of the article to delete") @PathVariable Long id){
        return articleService.deleteById(id);
    }

    // Image management endpoints
    
    /**
     * Upload image for article
     * 
     * @param file Image file to upload
     * @param type Image type (defaults to COVER)
     * @param alt Alternative text for the image
     * @return Upload result including image URL
     */
    @Operation(summary = "Upload article image", description = "Uploads an image for use in articles")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Image uploaded successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid image file")
    })
    @PostMapping("/images/upload")
    public ApiResponse uploadImage(
            @Parameter(description = "Image file to upload") @RequestParam("image") MultipartFile file,
            @Parameter(description = "Image type (defaults to COVER)") @RequestParam(defaultValue = "COVER") String type,
            @Parameter(description = "Alternative text for the image") @RequestParam(required = false) String alt) {
        return imageStorageService.upload(file, type, alt);
    }
}
