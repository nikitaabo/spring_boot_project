package com.example.spring.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;
import lombok.Data;

@Data
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    @JsonProperty("cartItems")
    private Set<CartItemDto> cartItemDtos;

}
