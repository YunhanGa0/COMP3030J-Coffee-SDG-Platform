package com.coffee_backend.controller;

import com.coffee_backend.dto.*;
import com.coffee_backend.service.FarmBlogService;
import com.coffee_backend.service.FarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/farms")
public class FarmController {

    @Autowired
    private FarmService farmService;

    @Autowired
    private FarmBlogService farmBlogService;

    @GetMapping
    public ApiResponse list(@ModelAttribute PageQueryRequest pageQueryRequest){
        return ApiResponse.success(farmService.list(pageQueryRequest));
    }

    @GetMapping("/{id}")
    public ApiResponse getById(@PathVariable Long id){
        return farmService.getById(id);
    }

    @PreAuthorize("hasAuthority('FARMER')")
    @PostMapping("/profile")
    public ApiResponse saveProfile(@RequestBody SaveProfileRequest saveProfileRequest){
        return farmService.saveProfile(saveProfileRequest);
    }

    @PreAuthorize("hasAuthority('FARMER')")
    @PutMapping("/update")
    public ApiResponse updateProfile(@RequestBody SaveProfileRequest saveProfileRequest){
        return farmService.updateProfile(saveProfileRequest);
    }

    @PreAuthorize("hasAuthority('FARMER')")
    @GetMapping("/status")
    public ApiResponse queryStatus(){
        return farmService.queryStatus();
    }

    @PostMapping("/{farmid}/blogs")
    public ApiResponse createBlog(@PathVariable Long farmid, @RequestBody FarmBlogRequest request){
        return farmBlogService.createBlog(farmid, request);
    }

    @PutMapping("/{farmid}/blogs/{blogid}")
    public ApiResponse updateBlog(@PathVariable Long farmid, @RequestBody FarmBlogRequest request, @PathVariable Long blogid){
        return farmBlogService.updateBlog(farmid, request, blogid);
    }

    @DeleteMapping("/{farmid}/blogs/{blogid}")
    public ApiResponse deleteBlog(@PathVariable Long farmid, @PathVariable Long blogid){
        return farmBlogService.deleteBlog(farmid, blogid);
    }

    @GetMapping("/{farmid}/blogs")
    public ApiResponse listBlog(@PathVariable Long farmid, @ModelAttribute PageQueryRequest request){
        return farmBlogService.listBlog(farmid, request);
    }

    @PatchMapping("/{farmid}/blogs/{blogid}/publish")
    public ApiResponse patchBlog(@PathVariable Long farmid, @RequestBody BlogPatchRequest request, @PathVariable Long blogid){
        return farmBlogService.patchBlog(farmid, request, blogid);
    }

    @GetMapping("/{farmid}/blogs/{blogid}")
    public ApiResponse getBlog(@PathVariable Long farmid, @PathVariable Long blogid){
        return farmBlogService.getBlog(farmid, blogid);
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('FARMER')")
    public ApiResponse getProfile(){
        return farmService.getProfile();
    }
}
