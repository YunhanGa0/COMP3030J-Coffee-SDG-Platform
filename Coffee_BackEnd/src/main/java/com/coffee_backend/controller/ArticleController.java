package com.coffee_backend.controller;

import cn.hutool.core.lang.UUID;
import com.coffee_backend.dto.ApiResponse;
import com.coffee_backend.dto.ArticleRequest;
import com.coffee_backend.dto.FileResponse;
import com.coffee_backend.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Value("${file.upload.path:uploads}")
    private String uploadPath;

    @Value("${file.access.url:http://localhost:8080}")
    private String accessUrl;

    @Autowired
    private ArticleService articleService;

    @GetMapping
    public ApiResponse list(){
        return articleService.list();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse save(@RequestBody ArticleRequest articleRequest){
        return articleService.save(articleRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse update(@PathVariable Long id, @RequestBody ArticleRequest articleRequest){
        return articleService.update(id, articleRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse delete(@PathVariable Long id){
        return articleService.deleteById(id);
    }

    @PostMapping("/images/upload")
    public ApiResponse uploadImage(@RequestParam("image") MultipartFile file, 
                                 @RequestParam(defaultValue = "COVER") String type, 
                                 @RequestParam(required = false) String alt) {
        if (file.isEmpty()) {
            return ApiResponse.error(400, "File can not be null");
        }

        try {
            // 创建上传目录
            Path uploadDir = Paths.get(uploadPath, type.toLowerCase());
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFileName = UUID.randomUUID().toString() + extension;

            // 保存文件
            Path filePath = uploadDir.resolve(newFileName);
            Files.copy(file.getInputStream(), filePath);

            // 生成访问URL
            String fileUrl = accessUrl + "/" + uploadPath + "/" + type.toLowerCase() + "/" + newFileName;

            FileResponse fileResponse = FileResponse.builder()
                    .url(fileUrl)
                    .type(type)
                    .build();

            return ApiResponse.success(fileResponse);

        } catch (IOException e) {
            e.printStackTrace();
            return ApiResponse.error(500, "File save failed: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(500, "Unexpected error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ApiResponse findById(@PathVariable Long id){
        return articleService.findById(id);
    }
}
