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
import ru.practicum.shareit.booking.dto.BookingDtoToItem;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.util.BookingMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
class BookingMapperTests {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Test
    @Sql(value = {"/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql"})
    void dtoToBookingTest() {
        BookingDto bookingDto = BookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(10))
                .build();
        Optional<User> user = userRepository.findById(1L);
        Optional<Item> item = itemRepository.findById(1L);

        Booking booking = BookingMapper.dtoToBooking(bookingDto, user.get(), item.get());

        Assertions.assertThat(booking)
                .hasFieldOrPropertyWithValue("booker", user.get())
                .hasFieldOrPropertyWithValue("item", item.get())
                .hasFieldOrProperty("start")
                .hasFieldOrProperty("end")
                .hasFieldOrPropertyWithValue("status", BookingState.WAITING);
    }

    @Test
    @Sql(value = {"/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql", "/test-create-booking.sql" })
    void bookingToDtoResponse() {
        Optional<Booking> booking = bookingRepository.findById(1L);

        BookingDtoResponse bookingDtoResponse = BookingMapper.bookingToResponse(booking.get());

        Assertions.assertThat(bookingDtoResponse)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrProperty("booker")
                .hasFieldOrProperty("item")
                .hasFieldOrProperty("start")
                .hasFieldOrProperty("end")
                .hasFieldOrPropertyWithValue("status", BookingState.WAITING);

    }

    @Test
    @Sql(value = {"/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql", "/test-create-booking.sql" })
    void bookingToBookingItem() {
        Optional<Booking> booking = bookingRepository.findById(1L);

        BookingDtoToItem bookingDtoResponse = BookingMapper.bookingDtoToItem(booking.get());

        Assertions.assertThat(bookingDtoResponse)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("bookerId", 1L);

    }
}
