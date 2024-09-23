package com.example.spring.repositories.order;

import com.example.spring.models.Order;
import com.example.spring.models.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
