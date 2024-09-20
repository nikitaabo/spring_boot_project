package com.example.spring.services;

import com.example.spring.dto.CartItemDto;
import com.example.spring.dto.CartItemUpdateDto;
import com.example.spring.dto.RequestCartItemDto;
import com.example.spring.dto.ShoppingCartDto;
import com.example.spring.models.User;

public interface ShoppingCartService {
    ShoppingCartDto getCart(String email);

    CartItemDto addCartItem(User user, RequestCartItemDto request);

    CartItemDto updateCartItemById(User user, Long id, CartItemUpdateDto request);

    void deleteById(Long id);

    void createShoppingCart(User user);
}
