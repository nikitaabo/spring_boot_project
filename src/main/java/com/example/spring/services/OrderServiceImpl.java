package com.example.spring.services;

import com.example.spring.dto.OrderDto;
import com.example.spring.dto.OrderItemDto;
import com.example.spring.dto.OrderRequestDto;
import com.example.spring.exception.EntityNotFoundException;
import com.example.spring.mapper.OrderItemMapper;
import com.example.spring.mapper.OrderMapper;
import com.example.spring.models.CartItem;
import com.example.spring.models.Order;
import com.example.spring.models.OrderItem;
import com.example.spring.models.ShoppingCart;
import com.example.spring.models.User;
import com.example.spring.models.enums.Status;
import com.example.spring.repositories.order.OrderRepository;
import com.example.spring.repositories.shopping.cart.ShoppingCartRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderRepository orderRepository;

    @Override
    public OrderDto placeOrder(OrderRequestDto request, User user) {
        Order order = orderMapper.toModel(request);
        order.setUser(user);
        order.setStatus(Status.PENDING);
        Set<OrderItem> orderItems = getOrderItems(user);
        orderItems.forEach(orderItem -> orderItem.setOrder(order));
        order.setOrderItems(orderItems);
        int sum = order.getOrderItems().stream()
                .mapToInt(o -> o.getPrice() * o.getQuantity())
                .sum();
        order.setTotal(BigDecimal.valueOf(sum));
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderDto> getOrders(User user) {
        return orderRepository.findByUser(user).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto updateOrder(Long id, Status newStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        order.setStatus(newStatus);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderItemDto> getOrderItemsOfOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderItemDto getOrderItemOfOrder(Long orderId, Long orderItemId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        OrderItem orderItemOfOrder = order.getOrderItems().stream()
                .filter(orderItem -> orderItem.getId().equals(orderItemId))
                .findFirst().orElseThrow(() -> new EntityNotFoundException("Order item not found"));
        return orderItemMapper.toDto(orderItemOfOrder);
    }

    private Set<OrderItem> getOrderItems(User user) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserEmail(user.getEmail());
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        Set<OrderItem> orderItems = cartItems.stream()
                .map(orderItemMapper::toOrderItemFromCartItem)
                .collect(Collectors.toSet());
        return orderItems;
    }
}
