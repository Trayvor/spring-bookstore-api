package org.example.bookstore.service;

import org.example.bookstore.dto.user.UserRegistrationRequestDto;
import org.example.bookstore.dto.user.UserResponseDto;
import org.example.bookstore.exception.RegistrationException;
import org.example.bookstore.model.User;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;

    User getAuthenticatedUser();
}
