package ru.practicum.shareit.item.service;


import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.stream.Stream;

public interface ItemService {
    ItemDto createItem(long ownerId, ItemDto item);

    Stream<Item> getAllItemsByOwnerId(long ownerId);

    ItemDto getItemById(long itemId);

    Item updateItem(long ownerId, long itemId, ItemDto itemDto);

    void removeItem(long ownerId, long itemId);

    Stream<Item> searchItems(String text);

    CommentDto createComment(CommentDto dto, long author, long itemId);
}
