package com.example.spring.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserLoginRequestDto(
        @NotEmpty
        @Email
        @Size(min = 8, max = 25)
        String email,
        @NotEmpty
        @Size(min = 8, max = 25)
        String password

) {
}
