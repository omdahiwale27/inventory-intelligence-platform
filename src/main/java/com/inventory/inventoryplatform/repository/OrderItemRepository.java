package com.inventory.inventoryplatform.repository;

import com.inventory.inventoryplatform.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}