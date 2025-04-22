package com.coffee_backend.controller;

import com.coffee_backend.dto.ApiResponse;
import com.coffee_backend.dto.CreateCoffeeBeanRequest;
import com.coffee_backend.entity.CoffeeBean;
import com.coffee_backend.service.CoffeeBeanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Le Liu
 * @create 2025-04
 */
@RestController
@RequestMapping("/api/farmers/coffee-beans")
public class CoffeeBeanController {
    @Autowired
    private CoffeeBeanService coffeeBeanService;

    /**
     * 农庄上传咖啡豆商品
     * POST /api/farmers/coffee-beans
     */
    @PreAuthorize("hasAuthority('FARMER')")
    @PostMapping
    public ApiResponse createCoffeeBean(@RequestBody CreateCoffeeBeanRequest request) {
        CoffeeBean coffeeBean = coffeeBeanService.createCoffeeBean(request);
        return ApiResponse.created( coffeeBean);
    }
}
