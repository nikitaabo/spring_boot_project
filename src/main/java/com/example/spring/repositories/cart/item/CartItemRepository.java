package com.example.spring.repositories.cart.item;

import com.example.spring.models.Book;
import com.example.spring.models.CartItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    boolean existsByBook(Book book);

    List<CartItem> findByBook(Book book);
}
