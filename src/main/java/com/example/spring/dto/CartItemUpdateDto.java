package com.example.spring.dto;

import jakarta.validation.constraints.Positive;

public record CartItemUpdateDto(@Positive int quantity) {
}
