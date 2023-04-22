package ru.practicum.shareit.UserTests;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql" })
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    void findAll() {
        List<User> users = userRepository.findAll();
        Assertions.assertThat(users).hasSize(3);
    }

    @Test
    void getUserById() {
        Optional<User> user = userRepository.findById(1L);
        Assertions.assertThat(user).hasValueSatisfying(f -> {
                    assertThat(f).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(f).hasFieldOrPropertyWithValue("name", "user");
                    assertThat(f).hasFieldOrPropertyWithValue("email", "user@gmail.com");
                }
        );
    }

    @Test
    void checkUserExisting() {
        boolean existing = userRepository.existsById(1L);
        assertThat(existing).isTrue();
    }
}
