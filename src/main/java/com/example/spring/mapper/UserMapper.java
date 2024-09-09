package com.example.spring.mapper;

import com.example.spring.config.MapperConfig;
import com.example.spring.dto.UserRegistrationRequestDto;
import com.example.spring.dto.UserResponseDto;
import com.example.spring.models.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    User toModel(UserRegistrationRequestDto registrationRequestDto);
}
