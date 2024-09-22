package com.example.spring.mapper;

import com.example.spring.config.MapperConfig;
import com.example.spring.dto.CartItemDto;
import com.example.spring.dto.RequestCartItemDto;
import com.example.spring.models.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {

    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemDto toDto(CartItem cartItem);

    CartItem toModel(RequestCartItemDto request);
}
