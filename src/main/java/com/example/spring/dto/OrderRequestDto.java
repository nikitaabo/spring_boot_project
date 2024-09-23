package com.example.spring.dto;

import jakarta.validation.constraints.NotBlank;

public record OrderRequestDto(@NotBlank String shippingAddress) {
}
