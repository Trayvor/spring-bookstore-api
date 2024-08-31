package org.example.bookstore.service;

import org.example.bookstore.dto.user.UserLoginRequestDto;
import org.example.bookstore.dto.user.UserLoginResponseDto;

public interface AuthenticationService {
    UserLoginResponseDto authenticate(UserLoginRequestDto userLoginRequestDto);
}
