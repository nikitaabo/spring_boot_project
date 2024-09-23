package com.example.spring.services;

import com.example.spring.dto.OrderDto;
import com.example.spring.dto.OrderItemDto;
import com.example.spring.dto.OrderRequestDto;
import com.example.spring.models.User;
import com.example.spring.models.enums.Status;
import java.util.List;

public interface OrderService {
    OrderDto placeOrder(OrderRequestDto request, User user);

    List<OrderDto> getOrders(User user);

    OrderDto updateOrder(Long id, Status newStatus);

    List<OrderItemDto> getOrderItemsOfOrder(Long id, User user);

    OrderItemDto getOrderItemOfOrder(Long orderId, Long orderItemId, User user);
}
