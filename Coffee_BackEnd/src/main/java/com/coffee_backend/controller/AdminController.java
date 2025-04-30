package com.coffee_backend.controller;

import com.coffee_backend.dto.ApiResponse;
import com.coffee_backend.dto.FinancialSupportRequest;
import com.coffee_backend.dto.SaveFarmerRequest;
import com.coffee_backend.dto.TechTrainingRequest;
import com.coffee_backend.enumType.ApplicationStatus;
import com.coffee_backend.enumType.CertificationStatus;
import com.coffee_backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/createFarmer")
    public ApiResponse saveFarmer(@RequestBody SaveFarmerRequest request){
        return adminService.saveFarmer(request);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/farmers")
    public ApiResponse getFarmers() {
        return adminService.getAllFarmers();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/trainings")
    public ApiResponse saveTechTraining(@RequestBody TechTrainingRequest request){
        return technicalTrainingService.saveTraining(request);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/trainings/{id}/cancel")
    public ApiResponse cancelTechTraining(@PathVariable Long id){
        return technicalTrainingService.cancelTechTraining(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/trainings/{id}")
    public ApiResponse deleteTechTraining(@PathVariable Long id){
        return technicalTrainingService.deleteTechTraining(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/trainings/{id}/applications")
    public ApiResponse getTechTrainingFarmers(@PathVariable Long id){
        return technicalTrainingService.getTechTrainingFarmers(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/financial-supports")
    public ApiResponse createFinancialSupport(@RequestBody FinancialSupportRequest request){
        return financialService.createFinancialSupport(request);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/financial-applications")
    public ApiResponse queryAllFinancialSupport(){
        return financialService.queryAllFinancialSupport();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/financial-applications/{id}/{review}")
    public ApiResponse reviewFinancialSupport(@PathVariable Long id){
        return financialService.reviewFinancialSupport(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/certification/applications")
    public ApiResponse queryCertificationApplication(@RequestParam(name = "status", required = false) CertificationStatus status){
        return certificationService.queryCertificationApplication(status);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/certification/applications/{id}/{review}")
    public ApiResponse reviewCertificationApplication(@PathVariable Long id){
        return certificationService.reviewCertificationApplication(id);
    }


}
