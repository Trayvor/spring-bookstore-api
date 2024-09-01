package org.example.bookstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.user.UserRegistrationRequestDto;
import org.example.bookstore.dto.user.UserResponseDto;
import org.example.bookstore.exception.EntityNotFoundException;
import org.example.bookstore.exception.RegistrationException;
import org.example.bookstore.mapper.UserMapper;
import org.example.bookstore.model.ShoppingCart;
import org.example.bookstore.model.User;
import org.example.bookstore.repository.shopping.cart.ShoppingCartRepository;
import org.example.bookstore.repository.user.UserRepository;
import org.example.bookstore.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("Can`t register user. Account with email "
                    + requestDto.getEmail() + " is already exists");
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        createShoppingCartForUser(user);
        return userMapper.toDto(user);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getName()).orElseThrow(
                () -> new EntityNotFoundException("Can`t find authenticated user with email "
                        + authentication.getName())
        );
    }

    private void createShoppingCartForUser(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }
}
