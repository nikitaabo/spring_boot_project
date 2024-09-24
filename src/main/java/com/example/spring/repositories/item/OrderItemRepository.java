package com.example.spring.repositories.item;

import com.example.spring.models.OrderItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findByIdAndOrderIdAndOrderUserId(Long id, Long orderId, Long userId);
}
