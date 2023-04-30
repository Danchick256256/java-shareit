package ru.practicum.shareit.booking.exception;

import javax.persistence.EntityExistsException;

public class BookingUnknownStateException extends EntityExistsException {
    public BookingUnknownStateException(Object identifier) {
        super("Unknown state: " + identifier.toString());
    }
}
