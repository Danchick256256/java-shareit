package ru.practicum.shareit.user.storage.InMemory;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;
import ru.practicum.shareit.util.NotFoundException;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class UserRepositoryInMemory implements UserRepository {
    private final ArrayList<User> userList;

    public UserRepositoryInMemory() {
        userList = new ArrayList<>();
    }

    @Override
    public Stream<User> getAll() {
        return userList.stream();
    }

    @Override
    public Optional<User> getById(int id) {
        return userList.stream().filter(user -> user.getId() == id).findFirst();
    }

    @Override
    public User save(User user) {
        userList.add(user);
        return userList.stream().filter(us -> us.getId() == user.getId()).findFirst().get();
    }

    @Override
    public User update(User user) {
        Optional<User> updatedUser = userList.stream().filter(us -> us.getId() == user.getId()).findFirst();
        if (updatedUser.isPresent()) {
            userList.remove(updatedUser.get());
            userList.add(user);
            return userList.stream().filter(us -> us.getId() == user.getId()).findFirst().get();
        } else {
            throw new NotFoundException("User not found by id: " + user.getId());
        }

    }

    @Override
    public void delete(int id) {
        Optional<User> deletedUser = userList.stream().filter(user -> id == user.getId()).findFirst();
        if (deletedUser.isPresent()) {
            userList.remove(deletedUser.get());
        } else {
            throw new NotFoundException("User not found by id: " + id);
        }
    }

    @Override
    public boolean emailExisting(String email) {
        Optional<String> u = userList.stream().map(User::getEmail).filter(userEmail -> Objects.equals(userEmail, email)).findFirst();
        return u.isPresent();
    }
}
