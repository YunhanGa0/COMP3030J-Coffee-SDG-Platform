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
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ApiResponse createOrder(@RequestBody CreateOrderRequest req) {

        Order order = orderService.createOrder(req);

        return ApiResponse.created(null);
    }

    /** 查询当前用户的全部订单 */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my")
    public ApiResponse listMyOrders() {
        List<Order> orders = orderService.listMyOrders();
        List<OrderResponse> orderResponses = orders.stream().map(order -> OrderResponse.builder()
                .id(order.getId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .orderTime(order.getOrderTime())
                .build()).toList();
        return ApiResponse.success(orderResponses);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ApiResponse getOrder(@PathVariable Long id) {
        Order order = orderService.getOrderDetail(id);
        OrderResponse orderResponse = OrderResponse.builder()
                .id(order.getId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .orderTime(order.getOrderTime())
                .build();
        return ApiResponse.success(orderResponse);
    }
}
