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
import com.coffee_backend.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for order management operations
 */
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

    /**
     * Create a new order for coffee beans
     *
     * @param req order creation request
     * @return order response with created order details
     * @throws NotFoundException if user or coffee bean not found
     * @throws BadRequestException if stock is insufficient
     */
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest req) {
        // Get current user ID
        Long userId = getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        // Get coffee bean price
        CoffeeBean coffeeBean = coffeeBeanRepository.findById(req.getCoffeeBeanId())
                .orElseThrow(() -> new NotFoundException("Coffee bean not found"));

        // Check stock
        if (coffeeBean.getBagStock() < req.getQuantity()) {
            throw new BadRequestException("Insufficient stock");
        }
        
        // Update stock
        coffeeBean.setBagStock(coffeeBean.getBagStock() - req.getQuantity());

        // Create order
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

    /**
     * Get orders for the current user
     *
     * @return list of order responses for the current user
     */
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

    /**
     * Get order detail for a specific order
     *
     * @param orderId ID of the order to retrieve
     * @return order response with detailed information
     * @throws NotFoundException if order not found
     * @throws ForbiddenException if user doesn't have permission to view the order
     */
    @Transactional(readOnly = true)
    public OrderResponse getOrderDetail(Long orderId) {
        Long currentUserId = getCurrentUserId();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
                
        if (!order.getUser().getId().equals(currentUserId)) {
            throw new ForbiddenException("No permission to view this order");
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

    /**
     * Get all orders for a farmer's products
     *
     * @return list of order responses for the farmer's products
     * @throws NotFoundException if farmer not found
     */
    @Transactional(readOnly = true)
    public List<OrderResponse> listOrdersForFarmer() {
        Long farmerId = getCurrentUserId();

        userRepository.findById(farmerId)
                .orElseThrow(() -> new NotFoundException("Farmer not found"));

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
     * Ship an order (change order status to SHIPPED)
     *
     * @param orderId ID of the order to ship
     * @throws NotFoundException if order not found
     * @throws ForbiddenException if the order doesn't belong to the farmer's farm
     * @throws BadRequestException if the order cannot be shipped in its current state
     */
    @Transactional
    public void shipOrder(Long orderId) {
        // Get current user (farmer) ID
        Long farmerId = getCurrentUserId();

        // Get order and verify ownership
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        // Verify farm ownership
        boolean belongsToFarmer = order.getCoffeeBean()
                .getFarm()
                .getUser()
                .getId()
                .equals(farmerId);

        if (!belongsToFarmer) {
            throw new ForbiddenException("This order doesn't belong to your farm");
        }

        // Only PAID orders can be shipped
        if (order.getStatus() != OrderStatus.PAID) {
            throw new BadRequestException("Order cannot be shipped in its current status");
        }

        // Update status
        order.setStatus(OrderStatus.SHIPPED);
        orderRepository.save(order);
    }

    /**
     * Get the current user ID from JWT token in request header
     *
     * @return current user ID
     */
    private Long getCurrentUserId() {
        String bearer = request.getHeader("Authorization");
        return jwtUtil.getUserIdFromToken(bearer.substring(7));
    }


}
