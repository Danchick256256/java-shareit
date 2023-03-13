package ru.practicum.shareit.item.util;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.util.GenerateUserId;

public class ItemMapper {
    public static Item dtoToItem(int ownerId, ItemDto itemDto) {
        return Item.builder()
                .ownerId(ownerId)
                .id(GenerateItemId.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }
}
