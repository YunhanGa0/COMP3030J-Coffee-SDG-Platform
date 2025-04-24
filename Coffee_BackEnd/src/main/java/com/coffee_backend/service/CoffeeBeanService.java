package com.coffee_backend.service;

import com.coffee_backend.dto.CreateCoffeeBeanRequest;
import com.coffee_backend.entity.CoffeeBean;
import com.coffee_backend.entity.Farm;
import com.coffee_backend.entity.User;
import com.coffee_backend.repo.CoffeeBeanRepository;
import com.coffee_backend.repo.FarmRepository;
import com.coffee_backend.repo.UserRepository;
import com.coffee_backend.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public CoffeeBean createCoffeeBean(CreateCoffeeBeanRequest request) {

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

        return coffeeBeanRepository.save(bean);
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
}
