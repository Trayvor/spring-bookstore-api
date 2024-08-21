package org.example.bookstore.service;

import org.example.bookstore.dto.UserRegistrationRequestDto;
import org.example.bookstore.dto.UserResponseDto;
import org.example.bookstore.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;
}
