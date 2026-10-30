package com.inventory.inventoryplatform.repository;

import com.inventory.inventoryplatform.entity.Order;
import com.inventory.inventoryplatform.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface OrderRepository extends JpaRepository<Order, Long> {

    long countByStatus(OrderStatus status);

    @Query("""
        SELECT COALESCE(SUM(o.totalAmount), 0)
        FROM Order o
        WHERE o.status IN ('CONFIRMED', 'COMPLETED')
    """)
    BigDecimal calculateTotalRevenue();
}