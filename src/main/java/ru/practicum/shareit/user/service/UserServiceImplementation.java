package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.exception.UserNotUniqueEmailException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(User user) {
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new UserNotUniqueEmailException(user.getEmail());
        }
    }

    @Override
    public User getUserById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(long id, User user) {
        User userUpdate = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        if (user.getName() != null) userUpdate.setName(user.getName());
        if (user.getEmail() != null) userUpdate.setEmail(user.getEmail());
        try {
            return userRepository.save(userUpdate);
        } catch (DataIntegrityViolationException ex) {
            throw new UserNotUniqueEmailException(user.getEmail());
        }
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }
}
