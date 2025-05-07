package com.coffee_backend.controller;

import com.coffee_backend.dto.ApiResponse;
import com.coffee_backend.dto.CreateOrderRequest;
import com.coffee_backend.dto.OrderResponse;
import com.coffee_backend.entity.Order;
import com.coffee_backend.entity.User;
import com.coffee_backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 创建订单（立即下单）
     * POST /api/orders
     */
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PostMapping
    public ApiResponse createOrder(@RequestBody CreateOrderRequest req) {
        OrderResponse orderResponse = orderService.createOrder(req);
        return ApiResponse.created(orderResponse);
    }

    /** 查询当前用户的全部订单 */
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping("/my")
    public ApiResponse listMyOrders() {
        List<OrderResponse> orders = orderService.listMyOrders();
        return ApiResponse.success(orders);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping("/{id}")
    public ApiResponse getOrder(@PathVariable Long id) {
        OrderResponse orderResponse = orderService.getOrderDetail(id);

        return ApiResponse.success(orderResponse);
    }
}
