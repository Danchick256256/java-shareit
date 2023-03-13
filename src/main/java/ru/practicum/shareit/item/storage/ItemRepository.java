package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Optional;
import java.util.stream.Stream;

public interface ItemRepository {
    Item save(int ownerId, ItemDto item);

    Stream<Item> getAll(int ownerId);

    Optional<Item> getById(int id);

    Item update(Item item);

    void delete(int id);

    Stream<Item> searchItem(String text);

}
