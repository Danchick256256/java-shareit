package ru.practicum.shareit.item.service;


import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(long ownerId, ItemDto item);

    List<ItemDto> getAllItemsByOwnerId(long ownerId);

    ItemDto getItemById(long itemId, long ownerId);

    ItemDto updateItem(long ownerId, long itemId, ItemDto itemDto);

    void removeItem(long ownerId, long itemId);

    List<ItemDto> searchItems(String text);

    CommentDto createComment(CommentDto dto, long author, long itemId);
}
