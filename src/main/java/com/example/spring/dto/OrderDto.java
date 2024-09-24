package com.example.spring.dto;

import com.example.spring.models.enums.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    @JsonProperty("orderItems")
    private Set<OrderItemDto> orderItemDtos;
    private LocalDateTime orderDate;
    private BigDecimal total;
    private Status status;
}
