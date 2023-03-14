package ru.practicum.shareit.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.util.EmailValidator;
import ru.practicum.shareit.util.BadRequestException;
import ru.practicum.shareit.util.ConflictRequestException;
import ru.practicum.shareit.util.NotFoundException;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        log.info("get.all.users.request");
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable int userId) {
        log.info("get.user.by.id.request");
        return userService.getUserById(userId);
    }

    @PostMapping
    public User createUser(@RequestBody UserDto userDto) {
        log.info("create.user.request");
        if (EmailValidator.isValid(userDto.getEmail())) {
            return userService.createUser(userDto);
        } else {
            throw new BadRequestException("email is wrong");
        }
    }

    @PatchMapping("/{userId}")
    public User updateUser(@PathVariable int userId, @RequestBody UserDto userDto) {
        log.info("update.user.request");
        return userService.updateUser(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable int userId) {
        log.info("delete.user.request");
        userService.removeUser(userId);
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> notFoundExceptionHandler() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> badRequestExceptionHandler() {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConflictRequestException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> conflictRequestExceptionHandler() {
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
}
