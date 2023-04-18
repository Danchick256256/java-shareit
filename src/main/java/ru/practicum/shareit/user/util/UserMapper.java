package ru.practicum.shareit.user.util;


import ru.practicum.shareit.user.DTO.UserDto;
import ru.practicum.shareit.user.model.User;

public class UserMapper {
    public static User toUser(UserDto userDto) {
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail()).build();
    }
}
