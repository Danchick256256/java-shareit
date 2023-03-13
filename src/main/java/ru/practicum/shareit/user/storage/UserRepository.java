package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.Optional;
import java.util.stream.Stream;

public interface UserRepository {
    Stream<User> getAll();
    Optional<User> getById(int id);
    User save(User user);
    User update(User user);
    void delete(int id);
    boolean emailExisting(String email);
}
