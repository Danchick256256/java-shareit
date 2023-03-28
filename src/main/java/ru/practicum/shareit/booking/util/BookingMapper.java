package ru.practicum.shareit.booking.util;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {
    public static Booking dtoToBooking(BookingDto bookingDto, User user, Item item) {
        return Booking.builder()
                .booker(user)
                .item(item)
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .status(BookingState.WAITING)
                .build();
    }
}
