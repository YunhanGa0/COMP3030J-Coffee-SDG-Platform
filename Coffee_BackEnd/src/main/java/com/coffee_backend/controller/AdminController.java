package com.coffee_backend.controller;

import com.coffee_backend.dto.*;
import com.coffee_backend.enumType.ApplicationStatus;
import com.coffee_backend.enumType.CertificationStatus;
import com.coffee_backend.enumType.TrainingStatus;
import com.coffee_backend.service.*;
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
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/trainings/update/{id}")
    public ApiResponse updateTrainingStatus(@PathVariable Long id, @RequestBody UpdateTrainingStatusRequest status){
        return technicalTrainingService.updateTrainingStatus(id, status);
    }

    /**
     * Delete a technical training program (admin only)
     * 
     * @param id Training ID to delete
     * @return Operation result
     */
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/trainings/{id}")
    public ApiResponse deleteTechTraining(@PathVariable Long id){
        return technicalTrainingService.deleteTechTraining(id);
    }

    /**
     * Get all farmers registered for a specific training (admin only)
     * 
     * @param id Training ID
     * @return List of registered farmers
     */
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/trainings/{id}/applications")
    public ApiResponse getTechTrainingFarmers(@PathVariable Long id){
        return technicalTrainingService.getTechTrainingFarmers(id);
    }

    // Financial support management

    /**
     * Create a financial support program (admin only)
     * 
     * @param request Financial support details
     * @return Operation result
     */
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
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/financial-applications")
    public ApiResponse queryFinancialSupport(@RequestParam(name = "status", required = false) ApplicationStatus status){
        return financialService.queryFinancialSupport(status);
    }

    /**
     * Review a financial support application (admin only)
     * 
     * @param id Application ID
     * @param request Review details
     * @return Operation result
     */
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/financial-applications/review/{id}")
    public ApiResponse reviewFinancialApplication(@PathVariable Long id, @RequestBody FinancialReviewRequest request){
        return financialService.reviewFinancialApplication(id, request);
    }

    // Certification management

    /**
     * Query certification applications (admin only)
     * 
     * @param status Optional filter by certification status
     * @return List of certification applications
     */
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/certification/applications")
    public ApiResponse queryCertificationApplication(@RequestParam(name = "status", required = false) CertificationStatus status){
        return certificationService.queryCertificationApplication(status);
    }

    /**
     * Review a certification application (admin only)
     * 
     * @param id Application ID
     * @param request Review details
     * @return Operation result
     */
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/certification/applications/review/{id}")
    public ApiResponse reviewCertificationApplication(@PathVariable Long id, @RequestBody CertificationReviewRequest request){
        return certificationService.reviewCertificationApplication(id, request);
    }

    // Customer management

    /**
     * Query total number of customers (admin only)
     * 
     * @return Customer count statistics
     */
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/queryCustomer")
    public ApiResponse queryCustomer(){
        return adminService.queryCustomer();
    }
}
