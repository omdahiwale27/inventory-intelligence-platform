package com.inventory.inventoryplatform.service;

import com.inventory.inventoryplatform.dto.OrderItemRequest;
import com.inventory.inventoryplatform.dto.OrderRequest;
import com.inventory.inventoryplatform.entity.Order;
import com.inventory.inventoryplatform.entity.OrderItem;
import com.inventory.inventoryplatform.entity.OrderStatus;
import com.inventory.inventoryplatform.entity.Product;
import com.inventory.inventoryplatform.repository.OrderItemRepository;
import com.inventory.inventoryplatform.repository.OrderRepository;
import com.inventory.inventoryplatform.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final InventoryService inventoryService;

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            ProductRepository productRepository,
            InventoryService inventoryService) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.inventoryService = inventoryService;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    public BigDecimal getTotalRevenue() {
        return orderRepository.calculateTotalRevenue();
    }
    public long getOrderCountByStatus(OrderStatus status) {
        return orderRepository.countByStatus(status);
    }
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Order not found with id: " + id
                        )
                );
    }

    public List<OrderItem> getOrderItems(Long orderId) {

        orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Order not found with id: " + orderId
                        )
                );

        return orderItemRepository.findByOrderId(orderId);
    }

    @Transactional
    public Order cancelOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Order not found with id: " + orderId
                        )
                );

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalArgumentException(
                    "Order is already cancelled"
            );
        }

        if (order.getStatus() != OrderStatus.CONFIRMED) {
            throw new IllegalArgumentException(
                    "Only confirmed orders can be cancelled"
            );
        }

        List<OrderItem> orderItems =
                orderItemRepository.findByOrderId(orderId);

        for (OrderItem orderItem : orderItems) {

            inventoryService.restoreStock(
                    orderItem.getProduct().getId(),
                    orderItem.getQuantity(),
                    order.getOrderNumber(),
                    "Stock restored after order cancellation"
            );
        }

        order.setStatus(OrderStatus.CANCELLED);

        return orderRepository.save(order);
    }

    @Transactional
    public Order createOrder(OrderRequest request) {

        if (request == null || request.getItems() == null
                || request.getItems().isEmpty()) {

            throw new IllegalArgumentException(
                    "Order must contain at least one item"
            );
        }

        Order order = Order.builder()
                .orderNumber(generateOrderNumber())
                .status(OrderStatus.PENDING)
                .totalAmount(BigDecimal.ZERO)
                .build();

        order = orderRepository.save(order);

        BigDecimal totalAmount = BigDecimal.ZERO;

        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequest itemRequest : request.getItems()) {

            Product product = productRepository.findById(
                    itemRequest.getProductId()
            ).orElseThrow(() ->
                    new IllegalArgumentException(
                            "Product not found with id: "
                                    + itemRequest.getProductId()
                    )
            );

            Integer quantity = itemRequest.getQuantity();

            if (quantity == null || quantity <= 0) {
                throw new IllegalArgumentException(
                        "Quantity must be greater than 0"
                );
            }

            if (product.getCurrentStock() < quantity) {
                throw new IllegalArgumentException(
                        "Insufficient stock for product: "
                                + product.getName()
                );
            }

            BigDecimal unitPrice = product.getSellingPrice();

            BigDecimal subtotal = unitPrice.multiply(
                    BigDecimal.valueOf(quantity)
            );

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(quantity)
                    .unitPrice(unitPrice)
                    .subtotal(subtotal)
                    .build();

            orderItems.add(orderItem);

            totalAmount = totalAmount.add(subtotal);
        }

        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.CONFIRMED);

        orderRepository.save(order);

        orderItemRepository.saveAll(orderItems);

        for (OrderItemRequest itemRequest : request.getItems()) {

            inventoryService.removeStock(
                    itemRequest.getProductId(),
                    itemRequest.getQuantity(),
                    order.getOrderNumber(),
                    "Stock deducted for customer order"
            );
        }

        return order;
    }

    private String generateOrderNumber() {

        return "ORD-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }
}