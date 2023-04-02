package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    Optional<Booking> findFirstByItemIdAndItemOwnerAndEndBeforeAndStatusOrderByEndDesc(long itemId, long ownerId, LocalDateTime timeEnd, BookingState state);

    Optional<Booking> findFirstByItemIdAndItemOwnerAndStartAfterAndStatusOrderByStartDesc(long itemId, long ownerId, LocalDateTime timeStart, BookingState state);

    @Query(value = "SELECT * FROM Bookings b " +
            "LEFT OUTER JOIN ITEMS item_1 " +
            "ON b.item = item_1.id " +
            "WHERE item_1.id = ?1 " +
            "AND item_1.owner = ?2 " +
            "AND b.stop < CURRENT_TIMESTAMP " +
            "ORDER BY b.stop desc limit 1", nativeQuery = true)
    Optional<Booking> findLastBooking(Long itemId, Long ownerId);

    @Query(value = "SELECT * FROM Bookings b " +
            "LEFT OUTER JOIN ITEMS item_1 " +
            "ON b.item = item_1.id " +
            "WHERE item_1.id = ?1 " +
            "AND item_1.owner = ?2 " +
            "AND b.start > CURRENT_TIMESTAMP " +
            "ORDER BY b.start asc limit 1", nativeQuery = true)
    Optional<Booking> findNextBooking(Long itemId, Long ownerId);
}
