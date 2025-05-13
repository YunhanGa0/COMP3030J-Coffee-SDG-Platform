package com.coffee_backend.controller;

import com.coffee_backend.dto.ApiResponse;
import com.coffee_backend.dto.CertificationApplicationRequest;
import com.coffee_backend.dto.FinancialApplicationRequest;
import com.coffee_backend.service.CertificationService;
import com.coffee_backend.service.FinancialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for farmer-specific operations.
 * Provides endpoints for financial support applications and certification processes.
 */
@RestController
@RequestMapping("/api/farmer")
@Tag(name = "Farmer Operations", description = "APIs for farmer-specific operations including financial support and certification applications")
public class FarmerController {
    @Autowired
    private FinancialService financialService;

    @Autowired
    private CertificationService certificationService;

    /**
     * Submit a financial support application (Farmer only)
     * @param request Financial application request containing application details
     * @return ApiResponse with the result of the operation
     */
    @Operation(summary = "Apply for financial support", description = "Allows farmers to submit applications for financial support. Only accessible by farmers.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Application submitted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Farmer privileges required")
    })
    @PreAuthorize("hasAuthority('FARMER')")
    @PostMapping("/financial-applications")
    public ApiResponse farmApplyFinancial(@RequestBody FinancialApplicationRequest request){
        return financialService.farmApplyFinancial(request);
    }

    /**
     * Retrieve all financial applications for the current farmer
     * @return ApiResponse containing the list of financial applications
     */
    @Operation(summary = "Get financial applications", description = "Retrieves all financial support applications submitted by the current farmer. Only accessible by farmers.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Applications retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Farmer privileges required")
    })
    @PreAuthorize("hasAuthority('FARMER')")
    @GetMapping("/financial-applications")
    public ApiResponse farmQueryFinancial(){
        return financialService.farmQueryFinancial();
    }

    /**
     * Retrieve all certification applications for the current farmer
     * @return ApiResponse containing the list of certification applications
     */
    @Operation(summary = "Get certification applications", description = "Retrieves all certification applications submitted by the current farmer. Only accessible by farmers.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Applications retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Farmer privileges required")
    })
    @PreAuthorize("hasAuthority('FARMER')")
    @GetMapping("/certification/applications")
    public ApiResponse queryApplicationCertification(){
        return certificationService.queryApplicationCertification();
    }

    /**
     * Submit a certification application
     * @param request Certification application request containing application details
     * @return ApiResponse with the result of the operation
     */
    @Operation(summary = "Apply for certification", description = "Allows farmers to submit applications for certification. Only accessible by farmers.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Application submitted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Farmer privileges required")
    })
    @PreAuthorize("hasAuthority('FARMER')")
    @PostMapping("/certification/applications")
    public ApiResponse applicationCertification(@RequestBody CertificationApplicationRequest request){
        return certificationService.applicationCertification(request);
    }
}
