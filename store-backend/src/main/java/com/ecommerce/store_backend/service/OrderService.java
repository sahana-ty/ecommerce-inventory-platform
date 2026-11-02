package com.ecommerce.store_backend.service;

import com.ecommerce.store_backend.model.*;
import com.ecommerce.store_backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    /**
     * CRITICAL TRANSACTIONAL BOUNDARY:
     * Ensures that stock deduction, order generation, and cart clearance
     * all succeed together or completely roll back if an error occurs.
     */
    @Transactional(rollbackFor = Exception.class)
    public Order placeOrder(Long userId) {
        // 1. Fetch User and their Cart
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart is empty or not found"));

        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cannot place an order with an empty cart.");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus("PENDING");

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        // 2. Validate Stock and Deduct Inventory
        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();
            int requestedQty = cartItem.getQuantity();

            if (product.getStockQuantity() < requestedQty) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            // Deduct stock
            product.setStockQuantity(product.getStockQuantity() - requestedQty);
            productRepository.save(product);

            // Create Order Item
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(requestedQty);
            orderItem.setPriceAtPurchase(product.getPrice());

            orderItems.add(orderItem);

            // Add to total amount (price * quantity)
            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(requestedQty)));
        }

        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItems);

        // 3. Save Order (Cascade will save OrderItems automatically)
        Order savedOrder = orderRepository.save(order);

        // 4. Clear the Cart after successful order placement
        cart.getCartItems().clear();
        cartRepository.save(cart);

        return savedOrder;
    }
}