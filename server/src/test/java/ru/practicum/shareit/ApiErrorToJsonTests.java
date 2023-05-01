package ru.practicum.shareit;

import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.http.HttpStatus;
import ru.practicum.shareit.exception.ApiError;

import java.util.Optional;

@JsonTest
class ApiErrorToJsonTests {
    @Autowired
    private JacksonTester<ApiError> json;

    @Test
    @SneakyThrows
    void userDtoTest() {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.OK)
                .message("message")
                .build();

        Optional<JsonContent<ApiError>> result = Optional.of(json.write(apiError));

        Assertions.assertThat(result)
                .isPresent()
                .hasValueSatisfying(i -> {
                    Assertions.assertThat(i)
                            .extractingJsonPathStringValue("$.status")
                            .isEqualTo("OK");
                    Assertions.assertThat(i)
                            .extractingJsonPathStringValue("$.error")
                            .isEqualTo("message");
                });
    }
}
