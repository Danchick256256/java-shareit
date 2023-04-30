package ru.practicum.shareit.user.exception;

import javax.persistence.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException(Object identifier) {
        super("{user.with.identifier:" + identifier.toString() + ".not.found}");
    }
}
