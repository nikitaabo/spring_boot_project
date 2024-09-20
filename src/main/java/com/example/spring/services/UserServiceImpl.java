package com.example.spring.services;

import com.example.spring.dto.UserRegistrationRequestDto;
import com.example.spring.dto.UserResponseDto;
import com.example.spring.exception.RegistrationException;
import com.example.spring.mapper.UserMapper;
import com.example.spring.models.Role;
import com.example.spring.models.User;
import com.example.spring.models.enums.RoleName;
import com.example.spring.repositories.role.RoleRepository;
import com.example.spring.repositories.shopping.cart.ShoppingCartRepository;
import com.example.spring.repositories.user.UserRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartService shoppingCartService;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto registrationRequestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(registrationRequestDto.getEmail())) {
            throw new RegistrationException("User with email " + registrationRequestDto.getEmail()
                    + " already exists");
        }
        User user = userMapper.toModel(registrationRequestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER);
        user.setRoles(Set.of(userRole));
        shoppingCartService.createShoppingCart(user);
        return userMapper.toDto(user);
    }
}
