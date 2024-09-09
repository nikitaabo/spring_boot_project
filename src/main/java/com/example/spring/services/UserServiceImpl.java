package com.example.spring.services;

import com.example.spring.dto.UserRegistrationRequestDto;
import com.example.spring.dto.UserResponseDto;
import com.example.spring.exception.RegistrationException;
import com.example.spring.mapper.UserMapper;
import com.example.spring.models.User;
import com.example.spring.repositories.user.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto registrationRequestDto)
            throws RegistrationException {
        Optional<User> userFromDb = userRepository.findByEmail(registrationRequestDto.getEmail());
        if (userFromDb.isPresent()) {
            throw new RegistrationException("User with email " + registrationRequestDto.getEmail()
                    + " already exists");
        }
        return userMapper.toDto(userRepository.save(userMapper.toModel(registrationRequestDto)));
    }
}
