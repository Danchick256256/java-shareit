package ru.practicum.shareit.user.service.InMemory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserRepository;
import ru.practicum.shareit.user.util.EmailValidator;
import ru.practicum.shareit.user.util.UserMapper;
import ru.practicum.shareit.util.BadRequestException;
import ru.practicum.shareit.util.ConflictRequestException;
import ru.practicum.shareit.util.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceInMemory implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAll().collect(Collectors.toList());
    }

    @Override
    public User getUserById(int id) {
        Optional<User> user = userRepository.getById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new NotFoundException("User not found");
        }
    }

    @Override
    public User createUser(UserDto userDto) {
        if (userRepository.emailExisting(userDto.getEmail())) {
            throw new ConflictRequestException("email is exists");
        }
        User user = UserMapper.dtoToUser(userDto);
        return userRepository.save(user);
    }

    @Override
    public User updateUser(int id, UserDto userDto) {
        if (userRepository.getById(id).isPresent()) {
            User updatedUser = userRepository.getById(id).get();
            if (!updatedUser.getEmail().equals(userDto.getEmail())) {
                if (userRepository.emailExisting(userDto.getEmail())) {
                    throw new ConflictRequestException("email is exists");
                }
            }
            if (userDto.getEmail() != null && !EmailValidator.isValid(userDto.getEmail())) {
                throw new BadRequestException("email is wrong");
            }
            if (userDto.getName() != null) {
                updatedUser.setName(userDto.getName());
            }
            if (userDto.getEmail() != null) {
                updatedUser.setEmail(userDto.getEmail());
            }
            return userRepository.update(updatedUser);
        } else {
            throw new NotFoundException("User not found");
        }
    }

    @Override
    public void removeUser(int userId) {
        userRepository.delete(userId);
    }
}
