package com.coffee_backend.controller;

import com.coffee_backend.dto.ApiResponse;
import com.coffee_backend.dto.CoffeeBeanDetailResponse;
import com.coffee_backend.entity.CoffeeBean;
import com.coffee_backend.service.CoffeeBeanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coffee-beans")
public class PublicController {

    @Autowired
    private CoffeeBeanService coffeeBeanService;

    /**
     * 获取某个咖啡豆的详情（商品页）
     * GET /api/coffee-beans/{id}
     */
    @GetMapping("/{id}")
    public ApiResponse getCoffeeBeanById(@PathVariable Long id) {
        CoffeeBean coffeeBean = coffeeBeanService.getCoffeeBeanById(id);
        CoffeeBeanDetailResponse dto = new CoffeeBeanDetailResponse();

        BeanUtils.copyProperties(coffeeBean, dto);

        return ApiResponse.success(dto);
    }
}
