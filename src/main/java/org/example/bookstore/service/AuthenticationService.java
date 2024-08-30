package org.example.bookstore.service;

import org.example.bookstore.dto.UserLoginRequestDto;
import org.example.bookstore.dto.UserLoginResponseDto;

public interface AuthenticationService {
    UserLoginResponseDto authenticate(UserLoginRequestDto userLoginRequestDto);
}
