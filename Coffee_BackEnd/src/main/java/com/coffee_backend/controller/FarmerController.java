package com.coffee_backend.controller;

import com.coffee_backend.dto.ApiResponse;
import com.coffee_backend.dto.CertificationApplicationRequest;
import com.coffee_backend.dto.FinancialApplicationRequest;
import com.coffee_backend.service.CertificationService;
import com.coffee_backend.service.FinancialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/farmer")
public class FarmerController {
    @Autowired
    private FinancialService financialService;

    @Autowired
    private CertificationService certificationService;

    //农民申请财政支持
    @PreAuthorize("hasAuthority('FARMER')")
    @PostMapping("/financial-applications")
    public ApiResponse farmApplyFinancial(@RequestBody FinancialApplicationRequest request){
        return financialService.farmApplyFinancial(request);
    }

    @PreAuthorize("hasAuthority('FARMER')")
    @GetMapping("/financial-applications")
    public ApiResponse farmQueryFinancial(){
        return financialService.farmQueryFinancial();
    }

    @PreAuthorize("hasAuthority('FARMER')")
    @GetMapping("/certification/applications")
    public ApiResponse queryApplicationCertification(){
        return certificationService.queryApplicationCertification();
    }

    @PreAuthorize("hasAuthority('FARMER')")
    @PostMapping("/certification/applications")
    public ApiResponse applicationCertification(@RequestBody CertificationApplicationRequest request){
        return certificationService.applicationCertification(request);
    }
}
