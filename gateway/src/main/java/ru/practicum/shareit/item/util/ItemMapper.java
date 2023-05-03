package ru.practicum.shareit.item.util;

import ru.practicum.shareit.booking.dto.BookingDtoToItem;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public class ItemMapper {
    public static Item dtoToItem(long ownerId, ItemDto itemDto) {
        return Item.builder()
                .owner(ownerId)
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .request(itemDto.getRequestId())
                .build();
    }

    public static ItemDto itemToDto(Item item, BookingDtoToItem lastBooking, BookingDtoToItem nextBooking, List<CommentDto> commentDtoList) {
        return ItemDto.builder()
                .id(item.getId())
                .ownerId(item.getOwner())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(commentDtoList)
                .requestId(item.getRequest())
                .build();
    }
}
