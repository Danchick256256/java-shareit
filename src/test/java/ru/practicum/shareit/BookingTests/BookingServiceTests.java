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
import ru.practicum.shareit.booking.exception.BookingBadRequestException;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.exception.BookingUnknownStateException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.exception.ItemBadRequestException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.exception.UserNotFoundException;

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
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql" })
    void createBookingEndBeforeNowTest() {
        BookingDto bookingDto = BookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().minusYears(1))
                .build();

        assertThrows(BookingBadRequestException.class, () -> bookingService.createBooking(2L, bookingDto));
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql" })
    void createBookingStartBeforeNowTest() {
        BookingDto bookingDto = BookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .build();

        assertThrows(BookingBadRequestException.class, () -> bookingService.createBooking(2L, bookingDto));
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql" })
    void createBookingItemIsUnavailableTest() {
        BookingDto bookingDto = BookingDto.builder()
                .itemId(2L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        assertThrows(ItemBadRequestException.class, () -> bookingService.createBooking(1L, bookingDto));
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql" })
    void createBookingStartEqualsEndNowTest() {
        LocalDateTime now = LocalDateTime.now();
        BookingDto bookingDto = BookingDto.builder()
                .itemId(1L)
                .start(now)
                .end(now)
                .build();

        assertThrows(BookingBadRequestException.class, () -> bookingService.createBooking(2L, bookingDto));
    }

    @Test
    @Sql(value = { "/test-schema.sql" })
    void createBookingUserNotFoundEndTest() {
        BookingDto bookingDto = BookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusDays(1))
                .build();

        assertThrows(UserNotFoundException.class, () -> bookingService.createBooking(2L, bookingDto));
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql" })
    void createBookingItemNotFoundEndTest() {
        BookingDto bookingDto = BookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusDays(1))
                .build();

        assertThrows(ItemNotFoundException.class, () -> bookingService.createBooking(2L, bookingDto));
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql" })
    void createBookingStartAfterEndTest() {
        BookingDto bookingDto = BookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusDays(10))
                .end(LocalDateTime.now().plusDays(1))
                .build();

        assertThrows(BookingBadRequestException.class, () -> bookingService.createBooking(2L, bookingDto));
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
    void getAllStateAll() {
        List<Booking> items = bookingService.getAll(1L, BookingState.ALL, 0L, 10L);

        Assertions.assertThat(items)
                .hasSize(5);

        Assertions.assertThat(items.get(0))
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql", "/test-create-booking.sql" })
    void getAllStatePast() {
        List<Booking> items = bookingService.getAll(1L, BookingState.PAST, 0L, 10L);

        Assertions.assertThat(items)
                .hasSize(0);

    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql", "/test-create-booking.sql" })
    void getAllStateCurrent() {
        List<Booking> items = bookingService.getAll(1L, BookingState.CURRENT, 0L, 10L);

        Assertions.assertThat(items)
                .hasSize(0);
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql", "/test-create-booking.sql" })
    void getAllStateFuture() {
        List<Booking> items = bookingService.getAll(1L, BookingState.FUTURE, 0L, 10L);

        Assertions.assertThat(items)
                .hasSize(5);

        Assertions.assertThat(items.get(0))
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql", "/test-create-booking.sql" })
    void getAllStateRejected() {
        List<Booking> items = bookingService.getAll(1L, BookingState.REJECTED, 0L, 10L);

        Assertions.assertThat(items)
                .hasSize(1);

        Assertions.assertThat(items.get(0))
                .hasFieldOrPropertyWithValue("id", 4L);
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql", "/test-create-booking.sql" })
    void getAllStateUnknownState() {
        assertThrows(BookingUnknownStateException.class, () -> bookingService.getAll(1L, BookingState.UNSUPPORTED_STATUS, 0L, 10L));
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql", "/test-create-booking.sql" })
    void getAllStateUnknown() {
        assertThrows(BookingNotFoundException.class, () -> bookingService.getAll(1L, BookingState.APPROVED, 0L, 10L));
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql", "/test-create-booking.sql" })
    void getAllUserNotFound() {
        assertThrows(UserNotFoundException.class, () -> bookingService.getAll(100L, BookingState.APPROVED, 0L, 10L));
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql", "/test-create-booking.sql" })
    void getAllUserZeroSize() {
        assertThrows(BookingBadRequestException.class, () -> bookingService.getAll(1L, BookingState.APPROVED, 0L, 0L));
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql", "/test-create-booking.sql" })
    void getAllUserWrongSize() {
        assertThrows(BookingBadRequestException.class, () -> bookingService.getAll(1L, BookingState.APPROVED, -10L, -10L));
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql", "/test-create-booking.sql" })
    void getAllByOwner() {
        List<Booking> items = bookingService.getAllByOwnerId(1L, BookingState.WAITING, 0L, 10L);

        Assertions.assertThat(items)
                .hasSize(1);

        Assertions.assertThat(items.get(0))
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql", "/test-create-booking.sql" })
    void getAllStateAllByOwner() {
        List<Booking> items = bookingService.getAllByOwnerId(1L, BookingState.ALL, 0L, 10L);

        Assertions.assertThat(items)
                .hasSize(6);

        Assertions.assertThat(items.get(0))
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql", "/test-create-booking.sql" })
    void getAllStatePastByOwner() {
        List<Booking> items = bookingService.getAllByOwnerId(1L, BookingState.PAST, 0L, 10L);

        Assertions.assertThat(items)
                .hasSize(1);

        Assertions.assertThat(items.get(0))
                .hasFieldOrPropertyWithValue("id", 3L);
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql", "/test-create-booking.sql" })
    void getAllStateCurrentByOwner() {
        List<Booking> items = bookingService.getAllByOwnerId(1L, BookingState.CURRENT, 0L, 10L);

        Assertions.assertThat(items)
                .hasSize(0);
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql", "/test-create-booking.sql" })
    void getAllStateFutureByOwner() {
        List<Booking> items = bookingService.getAllByOwnerId(1L, BookingState.FUTURE, 0L, 10L);

        Assertions.assertThat(items)
                .hasSize(5);

        Assertions.assertThat(items.get(0))
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql", "/test-create-booking.sql" })
    void getAllStateRejectedByOwner() {
        List<Booking> items = bookingService.getAllByOwnerId(1L, BookingState.REJECTED, 0L, 10L);

        Assertions.assertThat(items)
                .hasSize(1);

        Assertions.assertThat(items.get(0))
                .hasFieldOrPropertyWithValue("id", 4L);
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql", "/test-create-booking.sql" })
    void getAllStateUnknownStateByOwner() {
        assertThrows(BookingUnknownStateException.class, () -> bookingService.getAllByOwnerId(1L, BookingState.UNSUPPORTED_STATUS, 0L, 10L));
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql", "/test-create-booking.sql" })
    void getAllStateUnknownByOwner() {
        assertThrows(BookingNotFoundException.class, () -> bookingService.getAllByOwnerId(1L, BookingState.APPROVED, 0L, 10L));
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql", "/test-create-booking.sql" })
    void getAllUserNotFoundByOwner() {
        assertThrows(UserNotFoundException.class, () -> bookingService.getAllByOwnerId(100L, BookingState.APPROVED, 0L, 10L));
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql", "/test-create-booking.sql" })
    void getAllUserZeroSizeByOwner() {
        assertThrows(BookingBadRequestException.class, () -> bookingService.getAllByOwnerId(1L, BookingState.APPROVED, 0L, 0L));
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql", "/test-create-booking.sql" })
    void getAllUserWrongSizeByOwner() {
        assertThrows(BookingBadRequestException.class, () -> bookingService.getAllByOwnerId(1L, BookingState.APPROVED, -10L, -10L));
    }
}
