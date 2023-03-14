package ru.practicum.shareit.item.service;


import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.stream.Stream;

public interface ItemService {
    Item createItem(int ownerId, ItemDto item);

    Stream<Item> getAllItemsByOwnerId(int ownerId);

    Item getItemById(int itemId);

    Item updateItem(int ownerId, int itemId, ItemDto itemDto);

    void removeItem(int ownerId, int itemId);

    Stream<Item> searchItems(String text);

}
