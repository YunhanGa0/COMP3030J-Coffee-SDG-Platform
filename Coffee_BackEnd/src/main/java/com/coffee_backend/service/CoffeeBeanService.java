package com.coffee_backend.service;

import com.coffee_backend.dto.CoffeeBeanDetailResponse;
import com.coffee_backend.dto.CreateCoffeeBeanRequest;
import com.coffee_backend.entity.CoffeeBean;
import com.coffee_backend.entity.Farm;
import com.coffee_backend.entity.User;
import com.coffee_backend.repo.CoffeeBeanRepository;
import com.coffee_backend.repo.FarmRepository;
import com.coffee_backend.repo.UserRepository;
import com.coffee_backend.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Le Liu
 * @create 2025-04
 */
@Service
public class CoffeeBeanService {
    @Autowired
    private CoffeeBeanRepository coffeeBeanRepository;

    @Autowired
    private FarmRepository farmRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private HttpServletRequest request;

    @Transactional
    public CoffeeBeanDetailResponse createCoffeeBean(CreateCoffeeBeanRequest request) {

        Farm farm = farmRepository.findByUserId(getCurrentUserId())
                .orElseThrow(() -> new RuntimeException("当前用户未绑定农庄"));


        CoffeeBean bean = new CoffeeBean();
        bean.setFarm(farm);
        bean.setName(request.getName());
        bean.setVariety(request.getVariety());
        bean.setProcessMethod(request.getProcessMethod());
        bean.setRoastLevel(request.getRoastLevel());
        bean.setFlavorNotes(request.getFlavorNotes());
        bean.setWeightPerBagKg(request.getWeightPerBagKg());
        bean.setBagStock(request.getBagStock());
        bean.setPricePerBag(request.getPricePerBag());
        bean.setAvailable(request.getAvailable() != null ? request.getAvailable() : true);
        bean.setLimitedEdition(request.getLimitedEdition() != null ? request.getLimitedEdition() : false);
        bean.setImageUrl(request.getImageUrl());
        coffeeBeanRepository.save(bean);
        CoffeeBeanDetailResponse coffeeBeanDetailResponse = new CoffeeBeanDetailResponse();
        BeanUtils.copyProperties(bean, coffeeBeanDetailResponse);

        return coffeeBeanDetailResponse;
    }

    private Long getCurrentUserId() {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Long id = jwtUtil.getUserIdFromToken(token);

            return id;
        }
        throw new RuntimeException("未提供合法的Token");
    }

    @Transactional
    public void deleteCoffeeBean(Long beanId) {
        Long currentUserId = getCurrentUserId();

        // 查找咖啡豆并验证归属权限
        CoffeeBean bean = coffeeBeanRepository.findById(beanId)
                .orElseThrow(() -> new RuntimeException("该咖啡豆不存在"));

        // 校验该咖啡豆是否属于当前登录用户的农庄
        Farm farm = farmRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new RuntimeException("当前用户未绑定农庄"));

        if (!bean.getFarm().getId().equals(farm.getId())) {
            throw new RuntimeException("无权删除不属于你的咖啡豆");
        }

        coffeeBeanRepository.deleteById(beanId);
    }

    public CoffeeBean getCoffeeBeanById(Long id) {
        return coffeeBeanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到该咖啡豆"));
    }

    public List<CoffeeBeanDetailResponse> getCoffeeBeansList() {
        Long userId = getCurrentUserId();
        Farm farm = farmRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("用户未绑定农庄"));

        List<CoffeeBean> beans = coffeeBeanRepository.findByFarmId(farm.getId());

        return beans.stream().map(bean -> {
            CoffeeBeanDetailResponse dto = new CoffeeBeanDetailResponse();
            BeanUtils.copyProperties(bean, dto);
            return dto;
        }).toList();
    }

    @Transactional
    public void updateCoffeeBean(Long id, CreateCoffeeBeanRequest requestDto) {
        Long userId = getCurrentUserId();
        Farm farm = farmRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("当前用户未绑定农庄"));

        CoffeeBean bean = coffeeBeanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("咖啡豆不存在"));

        // 鉴权：确保该咖啡豆属于当前用户的农庄
        if (!bean.getFarm().getId().equals(farm.getId())) {
            throw new RuntimeException("无权限修改该咖啡豆信息");
        }

        // 更新字段
        bean.setName(requestDto.getName());
        bean.setVariety(requestDto.getVariety());
        bean.setProcessMethod(requestDto.getProcessMethod());
        bean.setRoastLevel(requestDto.getRoastLevel());
        bean.setFlavorNotes(requestDto.getFlavorNotes());
        bean.setWeightPerBagKg(requestDto.getWeightPerBagKg());
        bean.setBagStock(requestDto.getBagStock());
        bean.setPricePerBag(requestDto.getPricePerBag());
        bean.setAvailable(requestDto.getAvailable() != null ? requestDto.getAvailable() : true);
        bean.setLimitedEdition(requestDto.getLimitedEdition() != null ? requestDto.getLimitedEdition() : false);
        bean.setImageUrl(requestDto.getImageUrl());

        coffeeBeanRepository.save(bean);
    }

    /**
     * 根据农庄ID获取该农庄的所有咖啡豆
     */
    public List<CoffeeBeanDetailResponse> getCoffeeBeansByFarmId(Long farmId) {
        // 查询该农庄所有咖啡豆
        List<CoffeeBean> beans = coffeeBeanRepository.findByFarmId(farmId);

        return beans.stream().map(bean -> {
            CoffeeBeanDetailResponse dto = new CoffeeBeanDetailResponse();
            BeanUtils.copyProperties(bean, dto);
            return dto;
        }).toList();
    }


}
