package com.example.spring.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RequestCartItemDto(@Positive @NotNull Long bookId, @Positive int quantity) {
}
