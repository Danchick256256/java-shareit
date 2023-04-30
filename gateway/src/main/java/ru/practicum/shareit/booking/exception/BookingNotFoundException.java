package ru.practicum.shareit.booking.exception;

import javax.persistence.EntityNotFoundException;

public class BookingNotFoundException extends EntityNotFoundException {
    public BookingNotFoundException(Object identifier) {
        super("{booking.with.identifier:" + identifier.toString() + ".not.found}");
    }
}
