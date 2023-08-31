package com.bookshop.controller;

import com.bookshop.dto.order.OrderDto;
import com.bookshop.dto.order.OrderItemDto;
import com.bookshop.dto.order.PlaceOrderRequestDto;
import com.bookshop.model.Order;
import com.bookshop.model.User;
import com.bookshop.service.interf.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management", description = "Operations pertaining to orders in Order Management")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Place a new order")
    public OrderDto placeOrder(
            @RequestBody PlaceOrderRequestDto placeOrderRequestDto,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return orderService.placeOrder(user.getId(), placeOrderRequestDto);
    }

    @GetMapping
    @Operation(summary = "Get orders history")
    public List<OrderDto> getOrderHistory(
            Authentication authentication,
            Pageable pageable
    ) {
        User user = (User) authentication.getPrincipal();
        return orderService.getOrderHistory(user.getId(), pageable);
    }

    @PatchMapping("/{orderId}")
    @Operation(summary = "Update an order status")
    @PreAuthorize("hasRole('ADMIN')")
    public OrderDto updateOrderStatus(@PathVariable Long orderId, @RequestParam Order.Status status) {
        return orderService.updateOrderStatus(orderId, status);
    }

    @GetMapping("/{orderId}/items")
    @Operation(summary = "Get order items")
    public List<OrderItemDto> getAll(Authentication authentication,
                                     @PathVariable Long orderId) {
        User user = (User) authentication.getPrincipal();
        return orderService.getOrderItems(user.getId(), orderId);
    }
}
