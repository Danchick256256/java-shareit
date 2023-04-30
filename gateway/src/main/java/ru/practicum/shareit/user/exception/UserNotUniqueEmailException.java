package ru.practicum.shareit.user.exception;

import javax.persistence.EntityExistsException;

public class UserNotUniqueEmailException extends EntityExistsException {
    public UserNotUniqueEmailException(Object identifier) {
        super("{user.with.identifier:" + identifier.toString() + ".have.existing.email}");
    }
}
