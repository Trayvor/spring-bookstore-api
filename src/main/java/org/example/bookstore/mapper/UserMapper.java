package org.example.bookstore.mapper;

import org.example.bookstore.config.MapperConfig;
import org.example.bookstore.dto.UserRegistrationRequestDto;
import org.example.bookstore.dto.UserResponseDto;
import org.example.bookstore.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toModel(UserRegistrationRequestDto userRegistrationRequestDto);

    UserResponseDto toDto(User user);
}
