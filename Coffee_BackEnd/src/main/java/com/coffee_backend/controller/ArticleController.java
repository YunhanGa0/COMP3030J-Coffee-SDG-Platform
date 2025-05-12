package com.coffee_backend.controller;


import com.coffee_backend.dto.ApiResponse;
import com.coffee_backend.dto.ArticleRequest;

import com.coffee_backend.service.AliyunOssImageStorageService;
import com.coffee_backend.service.ArticleService;
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
    @GetMapping("/{id}")
    public ApiResponse findById(@PathVariable Long id){
        return articleService.findById(id);
    }

    /**
     * Create a new article (admin only)
     * 
     * @param articleRequest Article data
     * @return Operation result
     */
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
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse update(@PathVariable Long id, @RequestBody ArticleRequest articleRequest){
        return articleService.update(id, articleRequest);
    }

    /**
     * Delete an article (admin only)
     * 
     * @param id Article ID to delete
     * @return Operation result
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse delete(@PathVariable Long id){
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
    @PostMapping("/images/upload")
    public ApiResponse uploadImage(@RequestParam("image") MultipartFile file,
                                   @RequestParam(defaultValue = "COVER") String type,
                                   @RequestParam(required = false) String alt) {
        return imageStorageService.upload(file, type, alt);
    }
}
