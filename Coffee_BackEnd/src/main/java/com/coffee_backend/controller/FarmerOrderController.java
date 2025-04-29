package com.coffee_backend.controller;

import com.coffee_backend.dto.ApiResponse;
import com.coffee_backend.dto.OrderResponse;
import com.coffee_backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 农庄侧订单接口
 */
@RestController
@RequestMapping("/api/farmers")      // ← 统一前缀
public class FarmerOrderController {

    @Autowired
    private OrderService orderService;

    /**
     * GET /api/farmers/orders
     * 查询当前农庄收到的全部订单
     */
    @PreAuthorize("hasAuthority('FARMER')")
    @GetMapping("/orders")
    public ApiResponse listMyFarmOrders() {
        List<OrderResponse> orders = orderService.listOrdersForFarmer();
        return ApiResponse.success(orders);
    }

    /**
     * PUT /api/farmers/orders/{id}/ship
     * 农庄发货
     */
    @PreAuthorize("hasAuthority('FARMER')")
    @PutMapping("/orders/{id}/ship")
    public ApiResponse ship(@PathVariable Long id) {
        orderService.shipOrder(id);
        return ApiResponse.success();
    }
}
