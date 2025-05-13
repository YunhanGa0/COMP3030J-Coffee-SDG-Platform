package com.coffee_backend.controller;

import com.coffee_backend.dto.ApiResponse;
import com.coffee_backend.dto.CoffeeBeanDetailResponse;
import com.coffee_backend.dto.CreateCoffeeBeanRequest;
import com.coffee_backend.service.CoffeeBeanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Coffee Bean Controller
 * Handles operations for farmers to manage their coffee bean products
 */
@RestController
@RequestMapping("/api/farmers/coffee-beans")
@Tag(name = "Coffee Bean Management", description = "APIs for farmers to manage their coffee bean products")
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
    @Operation(summary = "Create coffee bean product", description = "Creates a new coffee bean product. Only accessible by farmers.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Coffee bean product created successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Farmer privileges required")
    })
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
    @Operation(summary = "Delete coffee bean product", description = "Deletes a coffee bean product. Only accessible by farmers who own the product.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Coffee bean product deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Farmer privileges required or not your product"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Coffee bean product not found")
    })
    @PreAuthorize("hasAuthority('FARMER')")
    @DeleteMapping("/{id}")
    public ApiResponse deleteCoffeeBean(@Parameter(description = "ID of the coffee bean product to delete") @PathVariable Long id) {
        coffeeBeanService.deleteCoffeeBean(id);
        return ApiResponse.success("Successfully deleted", null);
    }

    /**
     * Get list of all coffee bean products for the farmer
     * GET /api/farmers/coffee-beans
     * 
     * @return List of coffee bean products
     */
    @Operation(summary = "List coffee bean products", description = "Retrieves a list of all coffee bean products for the current farmer. Only accessible by farmers.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Coffee bean products retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Farmer privileges required")
    })
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
    @Operation(summary = "Update coffee bean product", description = "Updates a coffee bean product. Only accessible by farmers who own the product.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Coffee bean product updated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Farmer privileges required or not your product"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Coffee bean product not found")
    })
    @PreAuthorize("hasAuthority('FARMER')")
    @PutMapping("/{id}")
    public ApiResponse updateCoffeeBean(
            @Parameter(description = "ID of the coffee bean product to update") @PathVariable Long id,
            @RequestBody CreateCoffeeBeanRequest request
    ) {
        coffeeBeanService.updateCoffeeBean(id, request);
        return ApiResponse.success("Successfully updated", null);
    }
}
