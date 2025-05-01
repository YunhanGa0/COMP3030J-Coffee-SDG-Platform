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
        CoffeeBeanDetailResponse coffeeBean = coffeeBeanService.createCoffeeBean(request);
        return ApiResponse.created(coffeeBean);
    }


    /**
     * 农庄删除咖啡豆商品
     * DELETE /api/farmers/coffee-beans/{id}
     */
    @PreAuthorize("hasAuthority('FARMER')")
    @DeleteMapping("/{id}")
    public ApiResponse deleteCoffeeBean(@PathVariable Long id) {
        coffeeBeanService.deleteCoffeeBean(id);
        return ApiResponse.success("删除成功", null);
    }

    /**
     * 农庄获取咖啡豆商品列表
     * GET /api/farmers/coffee-beans
     */
    @PreAuthorize("hasAuthority('FARMER')")
    @GetMapping
    public ApiResponse getCoffeeBeansList() {
        return ApiResponse.success(coffeeBeanService.getCoffeeBeansList());
    }

    /**
     * 农庄修改咖啡豆商品
     * PUT /api/farmers/coffee-beans/{id}
     */
    @PreAuthorize("hasAuthority('FARMER')")
    @PutMapping("/{id}")
    public ApiResponse updateCoffeeBean(
            @PathVariable Long id,
            @RequestBody CreateCoffeeBeanRequest request
    ) {
        coffeeBeanService.updateCoffeeBean(id, request);
        return ApiResponse.success("修改成功", null);
    }
}
