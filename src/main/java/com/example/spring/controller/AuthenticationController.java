package com.example.spring.controller;

import com.example.spring.dto.UserRegistrationRequestDto;
import com.example.spring.dto.UserResponseDto;
import com.example.spring.exception.RegistrationException;
import com.example.spring.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;

    @PostMapping("/registration")
    public UserResponseDto registerUser(@RequestBody @Valid UserRegistrationRequestDto request)
            throws RegistrationException {
        return userService.register(request);
    }
}
