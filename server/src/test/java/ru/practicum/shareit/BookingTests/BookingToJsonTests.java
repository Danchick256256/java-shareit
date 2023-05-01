package ru.practicum.shareit.BookingTests;

import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.time.LocalDateTime;
import java.util.Optional;

@JsonTest
public class BookingToJsonTests {
    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    @SneakyThrows
    void bookingDtoTest() {
        LocalDateTime now = LocalDateTime.now();
        BookingDto itemDto = BookingDto.builder()
                .start(now)
                .end(now.plusDays(1))
                .itemId(1L)
                .build();

        Optional<JsonContent<BookingDto>> result = Optional.of(json.write(itemDto));

        Assertions.assertThat(result)
                .isPresent()
                .hasValueSatisfying(i -> {
                    Assertions.assertThat(i)
                            .hasJsonPathValue("$.start");
                    Assertions.assertThat(i)
                            .hasJsonPathValue("$.end");
                    Assertions.assertThat(i)
                            .extractingJsonPathNumberValue("$.itemId")
                            .isEqualTo(1);
                });
    }
}
