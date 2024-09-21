package com.example.spring.repositories.shopping.cart;

import com.example.spring.models.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    ShoppingCart findByUserEmail(String email);
}
