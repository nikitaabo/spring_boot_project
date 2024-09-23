package com.example.spring.controller;

import com.example.spring.dto.OrderDto;
import com.example.spring.dto.OrderItemDto;
import com.example.spring.dto.OrderRequestDto;
import com.example.spring.dto.StatusDto;
import com.example.spring.models.User;
import com.example.spring.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management", description = "Endpoints for managing orders")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/orders")
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
    @PostMapping
    @Operation(summary = "Place an order",
            description = "Place an order based on shopping cart's content")
    public OrderDto placeOrder(@RequestBody @Valid OrderRequestDto request,
                               Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.placeOrder(request, user);
    }

    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
    @GetMapping
    @Operation(summary = "Get all orders", description = "Get user's all orders")
    public List<OrderDto> getOrders(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.getOrders(user);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    @Operation(summary = "Update order", description = "Update a status of order")
    public OrderDto changeStatus(@PathVariable @Positive Long id,
                                 @RequestBody @Valid StatusDto newStatus) {
        return orderService.updateOrder(id, newStatus.status());
    }

    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}/items")
    @Operation(summary = "Get items of order", description = "Get items of certain order")
    public List<OrderItemDto> getOrderItems(@PathVariable @Positive Long id,
                                            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.getOrderItemsOfOrder(id, user);
    }

    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
    @GetMapping("/{orderId}/items/{orderItemId}")
    @Operation(summary = "Get item of order", description = "Get item of certain order")
    public OrderItemDto getOrderItemOfOrder(@PathVariable @Positive Long orderId,
                                            @PathVariable @Positive Long orderItemId,
                                            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.getOrderItemOfOrder(orderId, orderItemId, user);
    }
}

