package com.coffee_backend.service;

import com.coffee_backend.dto.CreateOrderRequest;
import com.coffee_backend.dto.OrderResponse;
import com.coffee_backend.entity.CoffeeBean;
import com.coffee_backend.entity.Order;
import com.coffee_backend.entity.User;
import com.coffee_backend.enumType.OrderStatus;
import com.coffee_backend.repo.CoffeeBeanRepository;
import com.coffee_backend.repo.OrderRepository;
import com.coffee_backend.repo.UserRepository;
import com.coffee_backend.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.coffee_backend.exception.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CoffeeBeanRepository coffeeBeanRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JwtUtil jwtUtil;

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest req) {
        // 获取当前用户 ID
        Long userId = getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("用户不存在"));

        // 查询豆子单价
        CoffeeBean coffeeBean = coffeeBeanRepository.findById(req.getCoffeeBeanId()).orElseThrow(() -> new NotFoundException("咖啡豆不存在"));

        // 库存
        if (coffeeBean.getBagStock() < req.getQuantity()) {
            throw new BadRequestException("库存不足");
        }
        // 库存-1
        coffeeBean.setBagStock(coffeeBean.getBagStock() - req.getQuantity());


        // 创建订单
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PAID);
        order.setCoffeeBean(coffeeBean);
        order.setQuantity(req.getQuantity());
        order.setContactNumber(req.getContactNumber());
        order.setShippingAddress(req.getShippingAddress());
        order.setRecipientName(req.getRecipientName());

        order = orderRepository.save(order);


        return OrderResponse.builder()
                .id(order.getId())
                .coffeeBeanId(coffeeBean.getId())
                .coffeeBeanName(coffeeBean.getName())
                .coffeeBeanImageUrl(order.getCoffeeBean().getImageUrl())
                .quantity(order.getQuantity())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .orderTime(order.getOrderTime())
                .shippingAddress(order.getShippingAddress())
                .contactNumber(order.getContactNumber())
                .recipientName(order.getRecipientName())
                .build();
    }

    /** 获取当前登录用户自己的订单 */
    @Transactional(readOnly = true)
    public List<OrderResponse> listMyOrders() {
        Long userId = getCurrentUserId();
        List<Order> orderList = orderRepository.findByUser_IdOrderByOrderTimeDesc(userId);
        return orderList.stream().map(order -> OrderResponse.builder()
                .id(order.getId())
                .coffeeBeanId(order.getCoffeeBean().getId())
                .coffeeBeanName(order.getCoffeeBean().getName())
                .coffeeBeanImageUrl(order.getCoffeeBean().getImageUrl())
                .quantity(order.getQuantity())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .orderTime(order.getOrderTime())
                .shippingAddress(order.getShippingAddress())
                .contactNumber(order.getContactNumber())
                .recipientName(order.getRecipientName())
                .build()).collect(Collectors.toList());
    }

    private Long getCurrentUserId() {
        String bearer = request.getHeader("Authorization");
        return jwtUtil.getUserIdFromToken(bearer.substring(7));
    }

    // 查询用户本人订单细节
    public OrderResponse getOrderDetail(Long orderId) {
        Long currentUserId = getCurrentUserId();

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("订单不存在"));
        if (!order.getUser().getId().equals(currentUserId)) {
            throw new ForbiddenException("无权限查看该订单");
        }
        return OrderResponse.builder()
                .id(order.getId())
                .coffeeBeanId(order.getCoffeeBean().getId())
                .coffeeBeanName(order.getCoffeeBean().getName())
                .coffeeBeanImageUrl(order.getCoffeeBean().getImageUrl())
                .quantity(order.getQuantity())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .orderTime(order.getOrderTime())
                .shippingAddress(order.getShippingAddress())
                .contactNumber(order.getContactNumber())
                .recipientName(order.getRecipientName())
                .build();




    }

    public List<OrderResponse> listOrdersForFarmer() {
        Long farmerId = getCurrentUserId();

        User farmer = userRepository.findById(farmerId)
                .orElseThrow(() -> new NotFoundException("农户不存在"));

        List<Order> orders = orderRepository.findAllForFarmer(farmerId);

        return orders.stream().map(order -> OrderResponse.builder()
                .id(order.getId())
                .coffeeBeanId(order.getCoffeeBean().getId())
                .coffeeBeanName(order.getCoffeeBean().getName())
                .coffeeBeanImageUrl(order.getCoffeeBean().getImageUrl())
                .quantity(order.getQuantity())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .orderTime(order.getOrderTime())
                .shippingAddress(order.getShippingAddress())
                .contactNumber(order.getContactNumber())
                .recipientName(order.getRecipientName())
                .build()).collect(Collectors.toList());

    }


    /**
     * 农户发货（将订单状态改为 SHIPPED）
     */
    @Transactional
    public void shipOrder(Long orderId) {

        // 当前登录用户（农户）ID
        Long farmerId = getCurrentUserId();

        // 取订单 & 校验归属
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("订单不存在"));

        // 农庄归属校验
        boolean belongsToFarmer = order.getCoffeeBean()
                .getFarm()
                .getUser()
                .getId()
                .equals(farmerId);


        if (!belongsToFarmer) {
            throw new ForbiddenException("该订单不属于你的农庄");
        }

        // 只有待发货(PAID) 才能发货
        if (order.getStatus() != OrderStatus.PAID) {
            throw new BadRequestException("当前状态不可发货");
        }

        // 更新状态
        order.setStatus(OrderStatus.SHIPPED);
        orderRepository.save(order);
    }

}
