package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {
    Booking createBooking(long ownerId, BookingDto bookingDto);

    Booking getById(long ownerId, long bookingId);

    List<Booking> getAll(long ownerId, BookingState state);

    List<Booking> getAll(long ownerId, BookingState state, Long from, Long size);

    List<Booking> getAllByOwnerId(long ownerId, BookingState state);

    List<Booking> getAllByOwnerId(long ownerId, BookingState state, Long from, Long size);

    BookingDtoResponse updateBooking(long ownerId, long bookingId, boolean approvedStatus);
}
