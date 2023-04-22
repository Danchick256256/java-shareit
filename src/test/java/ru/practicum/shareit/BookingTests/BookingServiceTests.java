package ru.practicum.shareit.BookingTests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
class BookingServiceTests {
    private final ItemService itemService;
    private final BookingService bookingService;

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql" })
    void createBookingTest() {
        BookingDto bookingDto = BookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusDays(1))
                .build();

        Optional<Booking> booking = Optional.of(bookingService.createBooking(2L, bookingDto));

        Assertions.assertThat(booking)
                .isPresent()
                .hasValueSatisfying(i -> {
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("id", 1L);
                    Assertions.assertThat(i).hasFieldOrProperty("booker");
                    Assertions.assertThat(i).hasFieldOrProperty("item");
                    Assertions.assertThat(i).hasFieldOrProperty("start");
                    Assertions.assertThat(i).hasFieldOrProperty("end");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("status", BookingState.WAITING);
                });
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql", "/test-create-booking.sql" })
    void updateStatusTest() {
        Optional<BookingDtoResponse> bookingDtoResponse = Optional.of(bookingService.updateBooking(1L, 1L, true));

        Assertions.assertThat(bookingDtoResponse)
                .isPresent()
                .hasValueSatisfying(i -> {
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("status", BookingState.APPROVED);
                });
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql" })
    void getUnknownBookingTest() {
        assertThrows(BookingNotFoundException.class, () -> bookingService.getById(1L, 10L));

    }

    @Test
    void getByWrongIdTest() {
        assertThrows(ItemNotFoundException.class, () -> itemService.getItemById(100L, 1L));
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql", "/test-create-booking.sql" })
    void getByOwnerIdTest() {
        Optional<Booking> bookingDtoResponse = Optional.of(bookingService.getById(1, 1));

        Assertions.assertThat(bookingDtoResponse)
                .isPresent()
                .hasValueSatisfying(i -> {
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("id", 1L);
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("status", BookingState.WAITING);
                    Assertions.assertThat(i).hasFieldOrProperty("booker");
                    Assertions.assertThat(i).hasFieldOrProperty("item");
                });
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql", "/test-create-booking.sql" })
    void getAll() {
        List<Booking> items = bookingService.getAll(1L, BookingState.WAITING, 0L, 10L);

        Assertions.assertThat(items)
                .hasSize(1);

        Assertions.assertThat(items.get(0))
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql", "/test-create-booking.sql" })
    void getAllByOwnerId() {
        List<Booking> items = bookingService.getAllByOwnerId(1L, BookingState.WAITING, 0L, 10L);

        Assertions.assertThat(items)
                .hasSize(1);

        Assertions.assertThat(items.get(0))
                .hasFieldOrPropertyWithValue("id", 1L);
    }
}
