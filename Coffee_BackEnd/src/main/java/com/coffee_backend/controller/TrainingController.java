package com.coffee_backend.controller;

import com.coffee_backend.dto.ApiResponse;
import com.coffee_backend.dto.PageQueryRequest;
import com.coffee_backend.service.TechnicalTrainingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for technical training management.
 * Provides endpoints for training programs listing, details, and application management.
 */
@RestController
@RequestMapping("/api/trainings")
@Tag(name = "Technical Trainings", description = "APIs for managing technical training programs")
public class TrainingController {

    @Autowired
    private TechnicalTrainingService technicalTrainingService;

    /**
     * Get paginated list of training programs
     * @param request Pagination and filtering parameters
     * @return ApiResponse containing paginated list of training programs
     */
    @Operation(summary = "List training programs", description = "Retrieves a paginated list of available technical training programs")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Training programs retrieved successfully")
    })
    @GetMapping
    public ApiResponse getList(@ModelAttribute PageQueryRequest request){
        return technicalTrainingService.getTrainingList(request);
    }

    /**
     * Get detailed information about a specific training program
     * @param id ID of the training program to retrieve
     * @return ApiResponse containing training program details
     */
    @Operation(summary = "Get training details", description = "Retrieves detailed information about a specific training program")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Training details retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Training program not found")
    })
    @GetMapping("/{id}")
    public ApiResponse getInfo(@Parameter(description = "ID of the training program") @PathVariable Long id){
        return technicalTrainingService.getTrainingInfo(id);
    }

    /**
     * Apply for a training program (Farmer only)
     * @param id ID of the training program to apply for
     * @return ApiResponse with the result of the application
     */
    @Operation(summary = "Apply for training", description = "Allows farmers to apply for a technical training program. Only accessible by farmers.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Application submitted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Farmer privileges required"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Training program not found")
    })
    @PreAuthorize("hasAuthority('FARMER')")
    @PostMapping("/{id}/apply")
    public ApiResponse applyTraining(@Parameter(description = "ID of the training program to apply for") @PathVariable Long id){
        return technicalTrainingService.applyTraining(id);
    }

    /**
     * Get number of applicants for a training program
     * @param id ID of the training program
     * @return ApiResponse containing the number of applicants
     */
    @Operation(summary = "Get training applicants count", description = "Retrieves the number of farmers who have applied for a specific training program")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Applicant count retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Training program not found")
    })
    @GetMapping("/applicants/{id}")
    public ApiResponse getTrainingApplicants(@Parameter(description = "ID of the training program") @PathVariable Long id){
        return technicalTrainingService.getTrainingApplicants(id);
    }
}
