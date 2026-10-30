
package com.inventory.inventoryplatform.repository;

import com.inventory.inventoryplatform.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySku(String sku);

    @Query("SELECT COALESCE(SUM(p.currentStock), 0) FROM Product p")
    long getTotalStockQuantity();

    @Query("""
        SELECT COUNT(p)
        FROM Product p
        WHERE p.active = true
        AND p.currentStock <= p.reorderLevel
        """)
    long countLowStockProducts();

    boolean existsBySku(String sku);
}
