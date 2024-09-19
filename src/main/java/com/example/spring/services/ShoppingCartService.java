package com.example.spring.services;

import com.example.spring.dto.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto getCart(String email);
}
