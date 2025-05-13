package com.coffee_backend.controller;

import com.coffee_backend.dto.ApiResponse;
import com.coffee_backend.dto.OrderResponse;
import com.coffee_backend.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for farm-side order management.
 * Provides endpoints for farmers to view and manage orders for their products.
 */
@RestController
@RequestMapping("/api/farmers")
@Tag(name = "Farmer Order Management", description = "APIs for farmers to manage orders for their products")
public class FarmerOrderController {

    @Autowired
    private OrderService orderService;

    /**
     * Retrieve all orders for the current farmer's farm
     * @return ApiResponse containing a list of orders for the farm
     */
    @Operation(summary = "List farm orders", description = "Retrieves all orders received by the current farmer's farm. Only accessible by farmers.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Farmer privileges required")
    })
    @PreAuthorize("hasAuthority('FARMER')")
    @GetMapping("/orders")
    public ApiResponse listMyFarmOrders() {
        List<OrderResponse> orders = orderService.listOrdersForFarmer();
        return ApiResponse.success(orders);
    }

    /**
     * Mark an order as shipped (Farmer only)
     * @param id ID of the order to mark as shipped
     * @return ApiResponse with the result of the operation
     */
    @Operation(summary = "Ship order", description = "Marks an order as shipped. Only accessible by farmers.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Order shipped successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Farmer privileges required"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PreAuthorize("hasAuthority('FARMER')")
    @PutMapping("/orders/{id}/ship")
    public ApiResponse ship(@Parameter(description = "ID of the order to ship") @PathVariable Long id) {
        orderService.shipOrder(id);
        return ApiResponse.success();
    }
}
