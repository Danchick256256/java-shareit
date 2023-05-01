package ru.practicum.shareit.booking.exception;

import javax.persistence.EntityExistsException;

public class BookingBadRequestException extends EntityExistsException {
    public BookingBadRequestException(Object identifier) {
        super("{booking.with.identifier:" + identifier.toString() + "}");
    }
}
