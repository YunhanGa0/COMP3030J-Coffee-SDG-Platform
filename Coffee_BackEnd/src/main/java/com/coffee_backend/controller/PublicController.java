package com.coffee_backend.controller;

import com.coffee_backend.dto.ApiResponse;
import com.coffee_backend.dto.CoffeeBeanDetailResponse;
import com.coffee_backend.entity.CoffeeBean;
import com.coffee_backend.service.CoffeeBeanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * Controller for publicly accessible endpoints.
 * Provides endpoints for retrieving information about coffee beans and farms
 * that do not require authentication.
 */
@RestController
@RequestMapping("/api")
@Tag(name = "Public Information", description = "APIs for publicly accessible information about coffee beans and farms")
public class PublicController {

    @Autowired
    private CoffeeBeanService coffeeBeanService;

    /**
     * Get detailed information about a specific coffee bean
     * @param id ID of the coffee bean to retrieve
     * @return ApiResponse containing detailed information about the coffee bean
     */
    @Operation(summary = "Get coffee bean details", description = "Retrieves detailed information about a specific coffee bean product")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Coffee bean details retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Coffee bean not found")
    })
    @GetMapping("coffee-beans/{id}")
    public ApiResponse getCoffeeBeanById(@Parameter(description = "ID of the coffee bean") @PathVariable Long id) {
        CoffeeBean coffeeBean = coffeeBeanService.getCoffeeBeanById(id);
        CoffeeBeanDetailResponse dto = new CoffeeBeanDetailResponse();

        BeanUtils.copyProperties(coffeeBean, dto);
        return ApiResponse.success(dto);
    }

    /**
     * List all coffee beans produced by a specific farm
     * @param farmId ID of the farm to get coffee beans for
     * @return ApiResponse containing a list of coffee beans from the specified farm
     */
    @Operation(summary = "List farm's coffee beans", description = "Retrieves a list of all coffee beans produced by a specific farm")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Coffee beans retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Farm not found")
    })
    @GetMapping("/{farmId}/coffee-beans")
    public ApiResponse getCoffeeBeansByFarmId(@Parameter(description = "ID of the farm") @PathVariable Long farmId) {
        return ApiResponse.success(coffeeBeanService.getCoffeeBeansByFarmId(farmId));
    }
}
