package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {
    Booking add(long ownerId, BookingDto bookingDto);

    Booking get(long ownerId, long bookingId);

    List<Booking> getAll(long ownerId, BookingState state, Long from, Long size);

    List<Booking> getAllByOwnerId(long ownerId, BookingState state, Long from, Long size);

    BookingDtoResponse approve(long ownerId, long bookingId, boolean approvedStatus);
}
