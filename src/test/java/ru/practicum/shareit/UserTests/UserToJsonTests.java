package ru.practicum.shareit.UserTests;

import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

@JsonTest
class UserToJsonTests {
    @Autowired
    private JacksonTester<User> json;

    @Test
    @SneakyThrows
    void userDtoTest() {
        User user = User.builder()
                .id(1L)
                .name("name")
                .email("1user@gmail.com")
                .build();

        Optional<JsonContent<User>> result = Optional.of(json.write(user));

        Assertions.assertThat(result)
                .isPresent()
                .hasValueSatisfying(i -> {
                    Assertions.assertThat(i)
                            .extractingJsonPathNumberValue("$.id")
                            .isEqualTo(1);
                    Assertions.assertThat(i)
                            .extractingJsonPathStringValue("$.name")
                            .isEqualTo("name");
                    Assertions.assertThat(i)
                            .extractingJsonPathStringValue("email")
                            .isEqualTo("1user@gmail.com");
                });
    }
}
