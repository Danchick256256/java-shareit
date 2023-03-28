package ru.practicum.shareit.item.util;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static Item dtoToItem(long ownerId, ItemDto itemDto) {
        return Item.builder()
                .owner(ownerId)
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }
}
