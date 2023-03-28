package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.stream.Stream;

public interface BookingService {
    Booking createBooking(long ownerId, BookingDto bookingDto);
    Booking getById(long ownerId, long bookingId);
    Stream<Booking> getAll(long ownerId, BookingState state);
    Stream<Booking> getAllBookingsByOwnerId(long ownerId, BookingState state);
    BookingDtoResponse updateBooking(long ownerId, long bookingId, boolean approvedStatus);
}
