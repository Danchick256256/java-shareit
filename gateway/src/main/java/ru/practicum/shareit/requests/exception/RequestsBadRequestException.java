package ru.practicum.shareit.requests.exception;

import javax.persistence.EntityExistsException;

public class RequestsBadRequestException extends EntityExistsException {
    public RequestsBadRequestException(Object identifier) {
        super("{request.with.identifier:" + identifier.toString() + "}");
    }
}
