package com.inventory.inventoryplatform.controller;

import com.inventory.inventoryplatform.entity.StockMovement;
import com.inventory.inventoryplatform.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/{productId}/add")
    @ResponseStatus(HttpStatus.CREATED)
    public StockMovement addStock(
            @PathVariable Long productId,
            @RequestParam Integer quantity,
            @RequestParam(required = false) String reference,
            @RequestParam(required = false) String notes) {

        return inventoryService.addStock(
                productId,
                quantity,
                reference,
                notes
        );
    }
}