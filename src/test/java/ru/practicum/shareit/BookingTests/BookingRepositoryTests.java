package ru.practicum.shareit.BookingTests;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql", "/test-create-booking.sql" })
public class BookingRepositoryTests {
    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void findAll() {
        List<Booking> bookings = bookingRepository.findAll();
        Assertions.assertThat(bookings).hasSize(2);
    }

    @Test
    void getBookingById() {
        Optional<Booking> booking = bookingRepository.findById(1L);
        Assertions.assertThat(booking).hasValueSatisfying(f -> {
                    assertThat(f).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(f).hasFieldOrProperty("start");
                    assertThat(f).hasFieldOrProperty("end");
                    assertThat(f).hasFieldOrProperty("booker");
                    assertThat(f).hasFieldOrProperty("item");
                    assertThat(f).hasFieldOrPropertyWithValue("status", BookingState.WAITING);
                }
        );
    }

    @Test
    void checkUserExisting() {
        boolean existing = bookingRepository.existsById(2L);
        assertThat(existing).isTrue();
    }

    @Test
    void findByBookerIdAndStatus() {
        List<Booking> bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartAsc(2L, BookingState.WAITING);
        assertThat(bookings).hasSize(1);
    }

    @Test
    void findAllByItemOwnerAndStartAfterOrderByStart() {
        List<Booking> bookings = bookingRepository.findAllByItemOwnerAndStartAfterOrderByStartDesc(2L, LocalDateTime.now().minusYears(1));
        assertThat(bookings).hasSize(1);
    }

    @Test
    void findAllByBookerIdAndEndBeforeOrderByStart() {
        List<Booking> bookings = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(2L, LocalDateTime.now().plusYears(1));
        assertThat(bookings).hasSize(1);
    }

    @Test
    void findAllByItemOwnerAndStatusOrderByStartAsc() {
        List<Booking> bookings = bookingRepository.findAllByItemOwnerAndStatusOrderByStartAsc(2L, BookingState.WAITING);
        assertThat(bookings).hasSize(1);
    }

    @Test
    void findAllByBookerIdOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(2L);
        assertThat(bookings).hasSize(1);
    }

    @Test
    void findAllByOwnerId() {
        List<Booking> bookings = bookingRepository.findAllByItemIdAndBookerId(1L, 1L);
        assertThat(bookings).hasSize(1);
    }
}
