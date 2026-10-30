
package com.inventory.inventoryplatform.controller;

import com.inventory.inventoryplatform.entity.OrderStatus;
import com.inventory.inventoryplatform.service.OrderService;
import com.inventory.inventoryplatform.service.DashboardService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final OrderService orderService;
    private final DashboardService dashboardService;

    public DashboardController(
            OrderService orderService,
            DashboardService dashboardService) {
        this.orderService = orderService;
        this.dashboardService = dashboardService;
    }

    @GetMapping("/order-summary")
    public Map<String, Object> getOrderSummary() {

        long pending = orderService.getOrderCountByStatus(OrderStatus.PENDING);
        long confirmed = orderService.getOrderCountByStatus(OrderStatus.CONFIRMED);
        long cancelled = orderService.getOrderCountByStatus(OrderStatus.CANCELLED);
        long completed = orderService.getOrderCountByStatus(OrderStatus.COMPLETED);

        Map<String, Object> summary = new LinkedHashMap<>();

        summary.put("totalOrders", pending + confirmed + cancelled + completed);
        summary.put("pendingOrders", pending);
        summary.put("confirmedOrders", confirmed);
        summary.put("cancelledOrders", cancelled);
        summary.put("completedOrders", completed);

        summary.put("totalRevenue", orderService.getTotalRevenue());

        summary.put("totalProducts", dashboardService.getTotalProducts());
        summary.put("totalStockQuantity", dashboardService.getTotalStockQuantity());
        summary.put("lowStockProducts", dashboardService.getLowStockProducts());

        return summary;
    }
}
