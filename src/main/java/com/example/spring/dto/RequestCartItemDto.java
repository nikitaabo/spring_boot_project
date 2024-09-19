package com.example.spring.dto;

import jakarta.validation.constraints.Positive;

public record RequestCartItemDto(@Positive Long bookId, @Positive int quantity) {
}
