package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerIdOrderByStartDesc(long ownerId);

    List<Booking> findAllByBookerIdAndStatusOrderByStartAsc(long ownerId, BookingState state);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(long ownerId, LocalDateTime time);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(long ownerId, LocalDateTime timeStart, LocalDateTime timeEnd);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(long ownerId, LocalDateTime time);

    List<Booking> findAllByItemOwnerOrderByStartDesc(long ownerId);

    List<Booking> findAllByItemOwnerAndStatusOrderByStartAsc(long ownerId, BookingState state);

    List<Booking> findAllByItemOwnerAndStartAfterOrderByStartDesc(long ownerId, LocalDateTime time);

    List<Booking> findAllByItemOwnerAndEndBeforeOrderByStartDesc(long ownerId, LocalDateTime time);

    List<Booking> findAllByItemOwnerAndStartBeforeAndEndAfterOrderByStartAsc(long ownerId, LocalDateTime timeStart, LocalDateTime timeEnd);

    Optional<Booking> findByIdAndBooker(long bookingId, long ownerId);

    List<Booking> findAllByItemIdAndBookerId(long itemId, long bookerId);
}
