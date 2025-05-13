package com.coffee_backend.controller;

import com.coffee_backend.dto.*;
import com.coffee_backend.enumType.ApplicationStatus;
import com.coffee_backend.enumType.CertificationStatus;
import com.coffee_backend.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Admin Controller
 * Handles administrative operations for farm management, trainings,
 * financial support, and certification processes
 */
@RestController
@RequestMapping("/api/admin")
@Tag(name = "Administration", description = "APIs for administrative operations including farm management, trainings, financial support, and certification processes")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private TechnicalTrainingService technicalTrainingService;

    @Autowired
    private FinancialService financialService;

    @Autowired
    private CertificationService certificationService;

    // Farmer account management

    /**
     * Create a farmer account (admin only)
     * 
     * @param request Farmer account details
     * @return Operation result
     */
    @Operation(summary = "Create farmer account", description = "Creates a new farmer account in the system. Only accessible by administrators.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Farmer account created successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Admin privileges required")
    })
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/createFarmer")
    public ApiResponse saveFarmer(@RequestBody SaveFarmerRequest request){
        return adminService.saveFarmer(request);
    }

    /**
     * Get all farmer accounts (admin only)
     * 
     * @return List of all farmers
     */
    @Operation(summary = "List all farmers", description = "Retrieves a list of all farmer accounts in the system. Only accessible by administrators.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Farmers retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Admin privileges required")
    })
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/farmers")
    public ApiResponse getFarmers() {
        return adminService.getAllFarmers();
    }

    // Technical training management

    /**
     * Create a technical training program (admin only)
     * 
     * @param request Training details
     * @return Operation result
     */
    @Operation(summary = "Create training program", description = "Creates a new technical training program. Only accessible by administrators.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Training program created successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Admin privileges required")
    })
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/trainings")
    public ApiResponse saveTechTraining(@RequestBody TechTrainingRequest request){
        return technicalTrainingService.saveTraining(request);
    }

    /**
     * Update training status (admin only)
     * 
     * @param id Training ID
     * @param status New status information
     * @return Operation result
     */
    @Operation(summary = "Update training status", description = "Updates the status of a technical training program. Only accessible by administrators.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Training status updated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Admin privileges required"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Training program not found")
    })
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/trainings/update/{id}")
    public ApiResponse updateTrainingStatus(
        @Parameter(description = "ID of the training program") @PathVariable Long id, 
        @RequestBody UpdateTrainingStatusRequest status){
        return technicalTrainingService.updateTrainingStatus(id, status);
    }

    /**
     * Delete a technical training program (admin only)
     * 
     * @param id Training ID to delete
     * @return Operation result
     */
    @Operation(summary = "Delete training program", description = "Deletes a technical training program. Only accessible by administrators.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Training program deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Admin privileges required"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Training program not found")
    })
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/trainings/{id}")
    public ApiResponse deleteTechTraining(@Parameter(description = "ID of the training program to delete") @PathVariable Long id){
        return technicalTrainingService.deleteTechTraining(id);
    }

    /**
     * Get all farmers registered for a specific training (admin only)
     * 
     * @param id Training ID
     * @return List of registered farmers
     */
    @Operation(summary = "List training applicants", description = "Retrieves a list of all farmers registered for a specific training program. Only accessible by administrators.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Applicants retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Admin privileges required"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Training program not found")
    })
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/trainings/{id}/applications")
    public ApiResponse getTechTrainingFarmers(@Parameter(description = "ID of the training program") @PathVariable Long id){
        return technicalTrainingService.getTechTrainingFarmers(id);
    }

    // Financial support management

    /**
     * Create a financial support program (admin only)
     * 
     * @param request Financial support details
     * @return Operation result
     */
    @Operation(summary = "Create financial support program", description = "Creates a new financial support program. Only accessible by administrators.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Financial support program created successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Admin privileges required")
    })
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/financial-supports")
    public ApiResponse createFinancialSupport(@RequestBody FinancialSupportRequest request){
        return financialService.createFinancialSupport(request);
    }

    /**
     * Query financial support applications (admin only)
     * 
     * @param status Optional filter by application status
     * @return List of financial support applications
     */
    @Operation(summary = "List financial applications", description = "Retrieves a list of financial support applications with optional status filtering. Only accessible by administrators.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Applications retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Admin privileges required")
    })
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/financial-applications")
    public ApiResponse queryFinancialSupport(
        @Parameter(description = "Optional filter by application status") 
        @RequestParam(name = "status", required = false) ApplicationStatus status){
        return financialService.queryFinancialSupport(status);
    }

    /**
     * Review a financial support application (admin only)
     * 
     * @param id Application ID
     * @param request Review details
     * @return Operation result
     */
    @Operation(summary = "Review financial application", description = "Reviews and processes a financial support application. Only accessible by administrators.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Application reviewed successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Admin privileges required"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Application not found")
    })
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/financial-applications/review/{id}")
    public ApiResponse reviewFinancialApplication(
        @Parameter(description = "ID of the application to review") 
        @PathVariable Long id, 
        @RequestBody FinancialReviewRequest request){
        return financialService.reviewFinancialApplication(id, request);
    }

    // Certification management

    /**
     * Query certification applications (admin only)
     * 
     * @param status Optional filter by certification status
     * @return List of certification applications
     */
    @Operation(summary = "List certification applications", description = "Retrieves a list of certification applications with optional status filtering. Only accessible by administrators.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Applications retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Admin privileges required")
    })
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/certification/applications")
    public ApiResponse queryCertificationApplication(
        @Parameter(description = "Optional filter by certification status") 
        @RequestParam(name = "status", required = false) CertificationStatus status){
        return certificationService.queryCertificationApplication(status);
    }

    /**
     * Review a certification application (admin only)
     * 
     * @param id Application ID
     * @param request Review details
     * @return Operation result
     */
    @Operation(summary = "Review certification application", description = "Reviews and processes a certification application. Only accessible by administrators.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Application reviewed successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Admin privileges required"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Application not found")
    })
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/certification/applications/review/{id}")
    public ApiResponse reviewCertificationApplication(
        @Parameter(description = "ID of the application to review") 
        @PathVariable Long id, 
        @RequestBody CertificationReviewRequest request){
        return certificationService.reviewCertificationApplication(id, request);
    }

    // Customer management

    /**
     * Query total number of customers (admin only)
     * 
     * @return Customer count statistics
     */
    @Operation(summary = "Get customer statistics", description = "Retrieves statistics about the total number of customers. Only accessible by administrators.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Customer statistics retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Admin privileges required")
    })
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/queryCustomer")
    public ApiResponse queryCustomer(){
        return adminService.queryCustomer();
    }
}
