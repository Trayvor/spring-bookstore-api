package org.example.bookstore.service.impl;

import jakarta.annotation.PostConstruct;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.user.UserRegistrationRequestDto;
import org.example.bookstore.dto.user.UserResponseDto;
import org.example.bookstore.exception.EntityNotFoundException;
import org.example.bookstore.exception.RegistrationException;
import org.example.bookstore.mapper.UserMapper;
import org.example.bookstore.model.Role;
import org.example.bookstore.model.User;
import org.example.bookstore.model.constant.RoleName;
import org.example.bookstore.repository.role.RoleRepository;
import org.example.bookstore.repository.user.UserRepository;
import org.example.bookstore.service.ShoppingCartService;
import org.example.bookstore.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ShoppingCartService shoppingCartService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    private Role userRole;

    @PostConstruct
    public void init() {
        userRole = roleRepository.findByRoleName(RoleName.ROLE_USER).orElseThrow(
                () -> new EntityNotFoundException("Can`t find role with name: "
                        + RoleName.ROLE_USER.name())
        );
    }

    @Transactional
    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("Can`t register user. Account with email "
                    + requestDto.getEmail() + " is already exists");
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(userRole));
        userRepository.save(user);
        shoppingCartService.createShoppingCart(user);
        return userMapper.toDto(user);
    }
}
