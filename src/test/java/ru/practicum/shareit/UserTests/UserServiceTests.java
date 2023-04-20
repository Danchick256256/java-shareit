package ru.practicum.shareit.UserTests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import ru.practicum.shareit.user.DTO.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.util.UserMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
class UserServiceTests {
    private final UserService userService;

    @Test
    @Order(0)
    @Sql(value = { "/test-schema.sql" })
    void createTest() {
        UserDto userCreateDto = UserDto.builder()
                .name("user")
                .email("user@gmail.com")
                .build();

        Optional<User> user = Optional.of(userService.createUser(UserMapper.toUser(userCreateDto)));

        assertThat(user)
                .isPresent()
                .hasValueSatisfying(f -> {
                            assertThat(f).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(f).hasFieldOrPropertyWithValue("name", "user");
                            assertThat(f).hasFieldOrPropertyWithValue("email", "user@gmail.com");
                        }
                );
    }

    @Test
    @Order(1)
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql" })
    void updateTest() {
        UserDto userUpdate = UserDto.builder()
                .name("userUpdated")
                .email("userUpdated@gmail.com")
                .build();

        Optional<User> userDto = Optional.of(userService.updateUser(1L, UserMapper.toUser(userUpdate)));

        assertThat(userDto)
                .isPresent()
                .hasValueSatisfying(f -> {
                            assertThat(f).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(f).hasFieldOrPropertyWithValue("name", "userUpdated");
                            assertThat(f).hasFieldOrPropertyWithValue("email", "userUpdated@gmail.com");
                        }
                );
    }

    @Test
    @Order(2)
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql" })
    void getByIdCorrectTest() {
        Optional<User> user = Optional.of(userService.getUserById(1L));

        assertThat(user)
                .isPresent()
                .hasValueSatisfying(f -> {
                            assertThat(f).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(f).hasFieldOrPropertyWithValue("name", "user");
                            assertThat(f).hasFieldOrPropertyWithValue("email", "user@gmail.com");
                        }
                );
    }

    @Test
    @Order(3)
    void getByIdUnCorrectTest() {
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(100L));
    }

    @Test
    @Order(4)
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql" })
    void getAllTest() {
        List<User> users = userService.getAllUsers();

        assertThat(users)
                .hasSize(2)
                .map(User::getId)
                .contains(1L, 2L);
    }

    @Test
    @Order(5)
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql" })
    void deleteByIdTest() {
        userService.deleteUser(1L);
        List<User> users = userService.getAllUsers();

        assertThat(users)
                .hasSize(1)
                .map(User::getId)
                .contains(2L);
    }

}
