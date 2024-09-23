package com.example.spring.mapper;

import com.example.spring.config.MapperConfig;
import com.example.spring.dto.OrderItemDto;
import com.example.spring.models.CartItem;
import com.example.spring.models.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(source = "book.price", target = "price")
    @Mapping(target = "id", ignore = true)
    OrderItem toOrderItemFromCartItem(CartItem cartItem);

    @Mapping(source = "book.id", target = "bookId")
    OrderItemDto toDto(OrderItem orderItem);
}
