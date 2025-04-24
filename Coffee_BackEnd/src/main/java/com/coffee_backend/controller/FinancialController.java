package com.coffee_backend.controller;

import com.coffee_backend.dto.ApiResponse;
import com.coffee_backend.service.FinancialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/financial-supports")
public class FinancialController {

    @Autowired
    private FinancialService financialService;

    @GetMapping
    public ApiResponse listSupports(){
        return financialService.listSupports();
    }

}
