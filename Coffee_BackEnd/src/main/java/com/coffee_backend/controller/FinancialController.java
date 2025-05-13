package com.coffee_backend.controller;

import com.coffee_backend.dto.ApiResponse;
import com.coffee_backend.service.FinancialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for financial support information.
 * Provides endpoints to retrieve information about available financial support programs.
 */
@RestController
@RequestMapping("/api/financial-supports")
@Tag(name = "Financial Support Information", description = "APIs for retrieving information about financial support programs")
public class FinancialController {

    @Autowired
    private FinancialService financialService;

    /**
     * List all available financial support programs
     * @return ApiResponse containing a list of available financial support programs
     */
    @Operation(summary = "List financial supports", description = "Retrieves a list of all available financial support programs")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Financial support information retrieved successfully")
    })
    @GetMapping
    public ApiResponse listSupports(){
        return financialService.listSupports();
    }

}
