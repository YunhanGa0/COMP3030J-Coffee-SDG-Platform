package com.coffee_backend.controller;

import com.coffee_backend.dto.ApiResponse;
import com.coffee_backend.dto.CoffeeBeanDetailResponse;
import com.coffee_backend.dto.CreateCoffeeBeanRequest;
import com.coffee_backend.entity.CoffeeBean;
import com.coffee_backend.service.CoffeeBeanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Coffee Bean Controller
 * Handles operations for farmers to manage their coffee bean products
 */
@RestController
@RequestMapping("/api/farmers/coffee-beans")
public class CoffeeBeanController {
    @Autowired
    private CoffeeBeanService coffeeBeanService;

    /**
     * Create a new coffee bean product (farmer only)
     * POST /api/farmers/coffee-beans
     * 
     * @param request Coffee bean product details
     * @return Created coffee bean product information
     */
    @PreAuthorize("hasAuthority('FARMER')")
    @PostMapping
    public ApiResponse createCoffeeBean(@RequestBody CreateCoffeeBeanRequest request) {
        CoffeeBeanDetailResponse coffeeBean = coffeeBeanService.createCoffeeBean(request);
        return ApiResponse.created(coffeeBean);
    }

    /**
     * Delete a coffee bean product (farmer only)
     * DELETE /api/farmers/coffee-beans/{id}
     * 
     * @param id Coffee bean product ID to delete
     * @return Operation result
     */
    @PreAuthorize("hasAuthority('FARMER')")
    @DeleteMapping("/{id}")
    public ApiResponse deleteCoffeeBean(@PathVariable Long id) {
        coffeeBeanService.deleteCoffeeBean(id);
        return ApiResponse.success("Successfully deleted", null);
    }

    /**
     * Get list of all coffee bean products for the farmer
     * GET /api/farmers/coffee-beans
     * 
     * @return List of coffee bean products
     */
    @PreAuthorize("hasAuthority('FARMER')")
    @GetMapping
    public ApiResponse getCoffeeBeansList() {
        return ApiResponse.success(coffeeBeanService.getCoffeeBeansList());
    }

    /**
     * Update a coffee bean product (farmer only)
     * PUT /api/farmers/coffee-beans/{id}
     * 
     * @param id Coffee bean product ID to update
     * @param request Updated coffee bean product details
     * @return Operation result
     */
    @PreAuthorize("hasAuthority('FARMER')")
    @PutMapping("/{id}")
    public ApiResponse updateCoffeeBean(
            @PathVariable Long id,
            @RequestBody CreateCoffeeBeanRequest request
    ) {
        coffeeBeanService.updateCoffeeBean(id, request);
        return ApiResponse.success("Successfully updated", null);
    }
}
