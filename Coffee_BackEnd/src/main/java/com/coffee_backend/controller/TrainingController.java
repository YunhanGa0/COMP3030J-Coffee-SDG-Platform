package com.coffee_backend.controller;

import com.coffee_backend.dto.ApiResponse;
import com.coffee_backend.dto.PageQueryRequest;
import com.coffee_backend.service.TechnicalTrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trainings")
public class TrainingController {

    @Autowired
    private TechnicalTrainingService technicalTrainingService;

    @GetMapping
    public ApiResponse getList(@ModelAttribute PageQueryRequest request){
        return technicalTrainingService.getTrainingList(request);
    }

    @GetMapping("/{id}")
    public ApiResponse getInfo(@PathVariable Long id){
        return technicalTrainingService.getTrainingInfo(id);
    }

    @PreAuthorize("hasAuthority('FARMER')")
    @PostMapping("/{id}/apply")
    public ApiResponse applyTraining(@PathVariable Long id){
        return technicalTrainingService.applyTraining(id);
    }
}
