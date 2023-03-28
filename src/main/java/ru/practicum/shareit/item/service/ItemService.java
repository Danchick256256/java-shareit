package ru.practicum.shareit.item.service;


import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.stream.Stream;

public interface ItemService {
    Item createItem(long ownerId, ItemDto item);

    Stream<Item> getAllItemsByOwnerId(long ownerId);

    Item getItemById(long itemId);

    Item updateItem(long ownerId, long itemId, ItemDto itemDto);

    void removeItem(long ownerId, long itemId);

    Stream<Item> searchItems(String text);

    Comment createComment(CommentDto dto, long author, long itemId);
}
