package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.DTO.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.util.UserMapper;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {
        log.info("get all users request");
        return userService.getAllUsers();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserById(@PathVariable long id) {
        log.info("get user by id request with id = {}", id);
        return userService.getUserById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody UserDto dto) {
        log.info("create user request with data = {}", dto.toString());
        return userService.createUser(UserMapper.toUser(dto));
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@RequestBody UserDto dto, @PathVariable long id) {
        log.info("update user by id request with id = {}, data = {}", id, dto.toString());
        return userService.updateUser(id, UserMapper.toUser(dto));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable long id) {
        log.info("delete user by id request with id = {}", id);
        userService.deleteUser(id);
    }
}

