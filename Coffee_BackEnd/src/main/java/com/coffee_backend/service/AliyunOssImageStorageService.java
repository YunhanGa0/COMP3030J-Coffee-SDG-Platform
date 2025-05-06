// AliyunOssImageStorageService.java
package com.coffee_backend.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.coffee_backend.dto.ApiResponse;
import com.coffee_backend.dto.FileResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class AliyunOssImageStorageService {

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    @Value("${aliyun.oss.baseUrl}")
    private String baseUrl;

    public ApiResponse upload(MultipartFile file, String type, String alt) {
        if (file.isEmpty()) {
            return ApiResponse.error(400, "File cannot be empty");
        }

        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString() + suffix;
        String objectKey = type.toLowerCase() + "/" + newFileName;

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            ossClient.putObject(new PutObjectRequest(bucketName, objectKey, file.getInputStream(), metadata));
        } catch (IOException e) {
            return ApiResponse.error(500, "Upload failed: " + e.getMessage());
        } finally {
            ossClient.shutdown();
        }

        String fileUrl = baseUrl + "/" + objectKey;
        FileResponse response = FileResponse.builder()
                .url(fileUrl)
                .type(type)
                .build();

        return ApiResponse.success(response);
    }
}
