package com.coffee_backend.controller;

import com.coffee_backend.dto.ApiResponse;
import com.coffee_backend.dto.CreateOrderRequest;
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
 * Controller for customer order management.
 * Provides endpoints for customers to create and view their orders.
 */
@RestController
@RequestMapping("/api/orders")
@Tag(name = "Customer Orders", description = "APIs for customers to manage their orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * Create a new order (Customer only)
     * @param req Order creation request containing product and quantity details
     * @return ApiResponse containing the created order details
     */
    @Operation(summary = "Create new order", description = "Creates a new order for products. Only accessible by customers.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Order created successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Customer privileges required"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request - product not available or insufficient quantity")
    })
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PostMapping
    public ApiResponse createOrder(@RequestBody CreateOrderRequest req) {
        OrderResponse orderResponse = orderService.createOrder(req);
        return ApiResponse.created(orderResponse);
    }

    /**
     * Retrieve all orders for the current customer
     * @return ApiResponse containing a list of the customer's orders
     */
    @Operation(summary = "List my orders", description = "Retrieves all orders placed by the current customer. Only accessible by customers.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Customer privileges required")
    })
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping("/my")
    public ApiResponse listMyOrders() {
        List<OrderResponse> orders = orderService.listMyOrders();
        return ApiResponse.success(orders);
    }

    /**
     * Retrieve details of a specific order (Customer only)
     * @param id ID of the order to retrieve
     * @return ApiResponse containing the order details
     */
    @Operation(summary = "Get order details", description = "Retrieves detailed information about a specific order. Only accessible by customers who placed the order.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Order details retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - Customer privileges required or not your order"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping("/{id}")
    public ApiResponse getOrder(@Parameter(description = "ID of the order to retrieve") @PathVariable Long id) {
        OrderResponse orderResponse = orderService.getOrderDetail(id);
        return ApiResponse.success(orderResponse);
    }
}
