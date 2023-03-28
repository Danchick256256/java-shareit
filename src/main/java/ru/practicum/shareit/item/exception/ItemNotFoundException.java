package ru.practicum.shareit.item.exception;

import javax.persistence.EntityNotFoundException;

public class ItemNotFoundException extends EntityNotFoundException {
    public ItemNotFoundException(Object identifier) {
        super("{item.with.identifier:" + identifier.toString() + ".not.found}");
    }
}
