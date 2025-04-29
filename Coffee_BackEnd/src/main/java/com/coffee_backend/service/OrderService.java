package com.coffee_backend.service;

import com.coffee_backend.dto.CreateOrderRequest;
import com.coffee_backend.dto.OrderItemResponse;
import com.coffee_backend.dto.OrderResponse;
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
import com.coffee_backend.exception.*;

import java.util.List;

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
        coffeeBean.setBagStock(coffeeBean.getBagStock() - req.getQuantity());
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

        OrderItemResponse orderItemResponse = OrderItemResponse.builder()
                .id(item.getId())
                .coffeeBeanId(item.getCoffeeBean().getId())
                .coffeeBeanName(item.getCoffeeBean().getName())
                .quantity(item.getQuantity())
                .pricePerBag(item.getPricePerBag())
                .subtotal(item.getSubtotal())
                .build();

        OrderResponse orderResponse = OrderResponse.builder()
                .id(order.getId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .orderTime(order.getOrderTime())
                .items(List.of(orderItemResponse))
                .build();
        return orderResponse;
    }

    /** 获取当前登录用户自己的订单 */
    @Transactional(readOnly = true)
    public List<OrderResponse> listMyOrders() {
        Long userId = getCurrentUserId();
        List<Order> orderList = orderRepository.findByBuyer_IdOrderByOrderTimeDesc(userId);
        List<OrderResponse> orderResponses = orderList.stream().map(order -> OrderResponse.builder()
                .id(order.getId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .orderTime(order.getOrderTime())
                .build()).toList();


        return orderResponses;
    }

    private Long getCurrentUserId() {
        String bearer = request.getHeader("Authorization");
        return jwtUtil.getUserIdFromToken(bearer.substring(7));
    }


    /** 查询当前用户某订单，若非本人则抛异常 */
    public OrderResponse getOrderDetail(Long orderId) {
        Long currentUserId = getCurrentUserId();

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("订单不存在"));
        if (!order.getBuyer().getId().equals(currentUserId)) {
            throw new ForbiddenException("无权限查看该订单");
        }
        OrderItem orderItem = orderItemRepository.findById(order.getItems().get(0).getId()).orElseThrow(() -> new NotFoundException("订单项不存在"));
        OrderItemResponse orderItemResponse = OrderItemResponse.builder()
                .id(orderItem.getId())
                .coffeeBeanId(orderItem.getCoffeeBean().getId())
                .coffeeBeanName(orderItem.getCoffeeBean().getName())
                .quantity(orderItem.getQuantity())
                .pricePerBag(orderItem.getPricePerBag())
                .subtotal(orderItem.getSubtotal())
                .build();
        OrderResponse orderResponse = OrderResponse.builder()
                .id(order.getId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .orderTime(order.getOrderTime())
                .items(List.of(orderItemResponse))
                .build();

        return orderResponse;

    }

    public List<OrderResponse> listOrdersForFarmer() {
        Long farmerId = getCurrentUserId();
        List<Order> orders = orderRepository.findAllForFarmer(farmerId);
        List<OrderResponse> orderResponses = orders.stream().map(order -> OrderResponse.builder()
                .id(order.getId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .orderTime(order.getOrderTime())
                .build()).toList();
        return orderResponses;
    }


    /**
     * 农户发货（将订单状态改为 SHIPPED）
     */
    @Transactional
    public void shipOrder(Long orderId) {

        // 1️⃣ 当前登录用户（农户）ID
        Long farmerId = getCurrentUserId();

        // 2️⃣ 取订单 & 校验归属
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("订单不存在"));

        // 农庄归属校验：只要订单里有一件商品来自该农户就允许发货
        boolean belongsToFarmer = order.getItems().stream()
                .anyMatch(item -> item.getCoffeeBean()
                        .getFarm().getUser().getId().equals(farmerId));

        if (!belongsToFarmer) {
            throw new ForbiddenException("该订单不属于你的农庄");
        }

        // 3️⃣ 只有待发货(PAID) 才能发货
        if (order.getStatus() != OrderStatus.PAID) {
            throw new BadRequestException("当前状态不可发货");
        }

        // 4️⃣ 更新状态
        order.setStatus(OrderStatus.SHIPPED);
        orderRepository.save(order);
    }

}
