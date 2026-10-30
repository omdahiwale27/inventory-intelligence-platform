
package com.inventory.inventoryplatform.service;

import com.inventory.inventoryplatform.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final ProductRepository productRepository;

    public DashboardService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public long getTotalProducts() {
        return productRepository.count();
    }

    public long getTotalStockQuantity() {
        return productRepository.getTotalStockQuantity();
    }

    public long getLowStockProducts() {
        return productRepository.countLowStockProducts();
    }
}
