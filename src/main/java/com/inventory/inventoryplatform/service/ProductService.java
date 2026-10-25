package com.inventory.inventoryplatform.service;

import com.inventory.inventoryplatform.dto.ProductRequest;
import com.inventory.inventoryplatform.entity.Product;
import com.inventory.inventoryplatform.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // CREATE PRODUCT
    public Product createProduct(ProductRequest request) {

        if (productRepository.existsBySku(request.getSku())) {
            throw new IllegalArgumentException(
                    "Product with SKU already exists: " + request.getSku()
            );
        }

        Product product = new Product();

        product.setSku(request.getSku());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCostPrice(request.getCostPrice());
        product.setSellingPrice(request.getSellingPrice());
        product.setCurrentStock(request.getCurrentStock());
        product.setReorderLevel(request.getReorderLevel());
        product.setActive(true);

        return productRepository.save(product);
    }

    // UPDATE PRODUCT
    public Product updateProduct(Long id, ProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Product not found with id: " + id
                        )
                );

        // Prevent duplicate SKU
        if (!product.getSku().equals(request.getSku())
                && productRepository.existsBySku(request.getSku())) {

            throw new IllegalArgumentException(
                    "Product with SKU already exists: " + request.getSku()
            );
        }

        product.setSku(request.getSku());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCostPrice(request.getCostPrice());
        product.setSellingPrice(request.getSellingPrice());
        product.setCurrentStock(request.getCurrentStock());
        product.setReorderLevel(request.getReorderLevel());

        // Make sure active is never null
        if (product.getActive() == null) {
            product.setActive(true);
        }

        return productRepository.save(product);
    }

    // DEACTIVATE PRODUCT
    public Product deactivateProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Product not found with id: " + id
                        )
                );

        product.setActive(false);

        return productRepository.save(product);
    }

    // GET ALL PRODUCTS
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // GET PRODUCT BY ID
    public Product getProductById(Long id) {

        return productRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Product not found with id: " + id
                        )
                );
    }
}