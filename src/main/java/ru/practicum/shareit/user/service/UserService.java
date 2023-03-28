package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User createUser(User user);
    User getUserById(long id);
    User updateUser(long id, User user);
    void deleteUser(long id);
    List<User> getAllUsers();
}
