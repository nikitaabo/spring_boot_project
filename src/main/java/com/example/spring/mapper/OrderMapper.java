package com.example.spring.mapper;

import com.example.spring.config.MapperConfig;
import com.example.spring.dto.OrderDto;
import com.example.spring.dto.OrderItemDto;
import com.example.spring.dto.OrderRequestDto;
import com.example.spring.models.Order;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    Order toModel(OrderRequestDto requestDto);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "orderItemDtos", source = "orderItems")
    OrderDto toDto(Order order);

    @AfterMapping
    default void setOrderItemDtos(@MappingTarget OrderDto orderDto,
                                 Order order, OrderItemMapper orderItemMapper) {
        Set<OrderItemDto> orderItemDtos = order.getOrderItems()
                .stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toSet());
        orderDto.setOrderItemDtos(orderItemDtos);
    }
}
