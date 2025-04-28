package com.coffee_backend.service;

import com.coffee_backend.dto.CreateOrderRequest;
import com.coffee_backend.entity.CoffeeBean;
import com.coffee_backend.entity.Order;
import com.coffee_backend.entity.OrderItem;
import com.coffee_backend.entity.User;
import com.coffee_backend.enumType.OrderStatus;
import com.coffee_backend.repo.CoffeeBeanRepository;
import com.coffee_backend.repo.OrderItemRepository;
import com.coffee_backend.repo.OrderRepository;
import com.coffee_backend.repo.UserRepository;
import com.coffee_backend.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private CoffeeBeanRepository coffeeBeanRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JwtUtil jwtUtil;

    @Transactional
    public Order createOrder(CreateOrderRequest req) {
        // 获取当前用户 ID
        Long userId = getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));

        // 查询豆子单价
        CoffeeBean coffeeBean = coffeeBeanRepository.findById(req.getCoffeeBeanId()).orElseThrow(() -> new RuntimeException("咖啡豆不存在"));

        Double totalAmount = coffeeBean.getPricePerBag() * req.getQuantity();


        // 创建订单
        Order order = new Order();
        order.setBuyer(user);
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.PAID);
        order = orderRepository.save(order);

        // 创建订单项
        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setCoffeeBean(coffeeBean);
        item.setQuantity(req.getQuantity());
        item.setPricePerBag(coffeeBean.getPricePerBag());
        item.setSubtotal(coffeeBean.getPricePerBag() * req.getQuantity());
        orderItemRepository.save(item);

        return order;
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

}
