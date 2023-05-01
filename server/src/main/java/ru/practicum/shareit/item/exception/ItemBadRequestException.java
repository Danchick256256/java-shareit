package ru.practicum.shareit.item.exception;

import javax.persistence.EntityExistsException;

public class ItemBadRequestException extends EntityExistsException {
    public ItemBadRequestException(Object identifier) {
        super("{item.with.identifier:" + identifier.toString() + "}");
    }
}
