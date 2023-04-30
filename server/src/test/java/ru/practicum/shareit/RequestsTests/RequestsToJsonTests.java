package ru.practicum.shareit.RequestsTests;

import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.requests.dto.RequestsDto;

import java.util.Optional;

@JsonTest
public class RequestsToJsonTests {
    @Autowired
    private JacksonTester<RequestsDto> json;

    @Test
    @SneakyThrows
    void userDtoTest() {
        RequestsDto requestsDto = RequestsDto.builder()
                .id(1L)
                .description("description")
                .build();

        Optional<JsonContent<RequestsDto>> result = Optional.of(json.write(requestsDto));

        Assertions.assertThat(result)
                .isPresent()
                .hasValueSatisfying(i -> {
                    Assertions.assertThat(i)
                            .extractingJsonPathNumberValue("$.id")
                            .isEqualTo(1);
                    Assertions.assertThat(i)
                            .extractingJsonPathStringValue("$.description")
                            .isEqualTo("description");
                });
    }
}
