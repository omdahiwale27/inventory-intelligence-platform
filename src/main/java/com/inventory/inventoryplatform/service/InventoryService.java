package com.inventory.inventoryplatform.service;

import com.inventory.inventoryplatform.entity.Product;
import com.inventory.inventoryplatform.entity.StockMovement;
import com.inventory.inventoryplatform.entity.StockMovementType;
import com.inventory.inventoryplatform.repository.ProductRepository;
import com.inventory.inventoryplatform.repository.StockMovementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {

    private final ProductRepository productRepository;
    private final StockMovementRepository stockMovementRepository;

    public InventoryService(
            ProductRepository productRepository,
            StockMovementRepository stockMovementRepository) {

        this.productRepository = productRepository;
        this.stockMovementRepository = stockMovementRepository;
    }

    @Transactional
    public StockMovement addStock(
            Long productId,
            Integer quantity,
            String reference,
            String notes) {

        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException(
                    "Stock quantity must be greater than 0"
            );
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Product not found with id: " + productId
                        )
                );

        int previousStock = product.getCurrentStock();
        int newStock = previousStock + quantity;

        product.setCurrentStock(newStock);

        productRepository.save(product);

        StockMovement movement = StockMovement.builder()
                .product(product)
                .type(StockMovementType.PURCHASE)
                .quantity(quantity)
                .previousStock(previousStock)
                .newStock(newStock)
                .reference(reference)
                .notes(notes)
                .createdAt(java.time.LocalDateTime.now())
                .build();

        return stockMovementRepository.save(movement);
    }
}