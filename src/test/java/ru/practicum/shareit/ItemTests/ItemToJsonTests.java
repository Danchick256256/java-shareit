package ru.practicum.shareit.ItemTests;

import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collections;
import java.util.Optional;

@JsonTest
public class ItemToJsonTests {
    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    @SneakyThrows
    void userDtoTest() {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("name")
                .requestId(1L)
                .comments(Collections.emptyList())
                .ownerId(1L)
                .description("description")
                .build();

        Optional<JsonContent<ItemDto>> result = Optional.of(json.write(itemDto));

        Assertions.assertThat(result)
                .isPresent()
                .hasValueSatisfying(i -> {
                    Assertions.assertThat(i)
                            .extractingJsonPathNumberValue("$.id")
                            .isEqualTo(1);
                    Assertions.assertThat(i)
                            .extractingJsonPathStringValue("$.description")
                            .isEqualTo("description");
                    Assertions.assertThat(i)
                            .extractingJsonPathStringValue("$.name")
                            .isEqualTo("name");
                    Assertions.assertThat(i)
                            .extractingJsonPathNumberValue("$.requestId")
                            .isEqualTo(1);
                    Assertions.assertThat(i)
                            .extractingJsonPathNumberValue("$.ownerId")
                            .isEqualTo(1);
                });
    }
}
