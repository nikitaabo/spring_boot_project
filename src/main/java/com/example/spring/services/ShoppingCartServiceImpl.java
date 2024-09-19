package com.example.spring.services;

import com.example.spring.dto.ShoppingCartDto;
import com.example.spring.mapper.ShoppingCartMapper;
import com.example.spring.repositories.shopping.cart.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;

    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    public ShoppingCartDto getCart(String email) {
        return shoppingCartMapper.toDto(shoppingCartRepository.findByUser_Email(email));
    }

}
