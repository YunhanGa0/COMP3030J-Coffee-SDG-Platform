package com.coffee_backend.controller;

import com.coffee_backend.dto.ApiResponse;
import com.coffee_backend.dto.FinancialApplicationRequest;
import com.coffee_backend.service.FinancialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/farmer")
public class FarmerController {
    @Autowired
    private FinancialService financialService;

    @PreAuthorize("hasAuthority('FARMER')")
    @PostMapping("/financial-applications")
    public ApiResponse farmApplyFinancial(@RequestBody FinancialApplicationRequest request){
        return financialService.farmApplyFinancial(request);
    }
}
