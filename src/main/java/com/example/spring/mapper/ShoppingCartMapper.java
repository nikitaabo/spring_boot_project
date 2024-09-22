package com.example.spring.mapper;

import com.example.spring.config.MapperConfig;
import com.example.spring.dto.CartItemDto;
import com.example.spring.dto.ShoppingCartDto;
import com.example.spring.models.ShoppingCart;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "cartItems", target = "cartItemDtos")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    @AfterMapping
    default void setCartItemDtos(@MappingTarget ShoppingCartDto cartDto,
                                 ShoppingCart cart, CartItemMapper cartItemMapper) {
        Set<CartItemDto> cartItemDtos = cart.getCartItems()
                .stream()
                .map(cartItemMapper::toDto)
                .collect(Collectors.toSet());
        cartDto.setCartItemDtos(cartItemDtos);
    }
}
