package org.example.bookstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.UserRegistrationRequestDto;
import org.example.bookstore.dto.UserResponseDto;
import org.example.bookstore.exception.RegistrationException;
import org.example.bookstore.mapper.UserMapper;
import org.example.bookstore.model.User;
import org.example.bookstore.repository.user.UserRepository;
import org.example.bookstore.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Can`t register user. Account with email "
                    + requestDto.getEmail() + " is already exists");
        }
        User userModel = userMapper.toModel(requestDto);
        userRepository.save(userModel);
        return userMapper.toDto(userModel);
    }
}
