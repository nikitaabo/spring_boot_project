package com.example.spring.dto;

import com.example.spring.models.enums.Status;
import jakarta.validation.constraints.NotNull;

public record StatusDto(@NotNull Status status) {
}
