package ru.practicum.shareit.requests.exception;

import javax.persistence.EntityNotFoundException;

public class RequestsNotFoundException extends EntityNotFoundException {
    public RequestsNotFoundException(Object identifier) {
        super("{request.with.identifier:" + identifier.toString() + ".not.found}");
    }
}
