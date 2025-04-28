package com.coffee_backend.controller;

import com.coffee_backend.dto.ApiResponse;
import com.coffee_backend.dto.CreateOrderRequest;
import com.coffee_backend.entity.Order;
import com.coffee_backend.entity.User;
import com.coffee_backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 创建订单（立即下单）
     * POST /api/orders
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ApiResponse createOrder(@RequestBody CreateOrderRequest req) {

        Order order = orderService.createOrder(req);

        return ApiResponse.created(null);
    }
}
