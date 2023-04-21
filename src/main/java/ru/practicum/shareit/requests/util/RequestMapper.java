package ru.practicum.shareit.requests.util;

import ru.practicum.shareit.booking.dto.BookingDtoToItem;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.dto.RequestsDto;
import ru.practicum.shareit.requests.dto.RequestsResponse;
import ru.practicum.shareit.requests.model.Requests;

import java.time.LocalDateTime;
import java.util.List;

public class RequestMapper {
    public static Requests dtoToRequest(RequestsDto requestsDto, long ownerId) {
        return Requests.builder()
                .owner(ownerId)
                .description(requestsDto.getDescription())
                .creationDate(LocalDateTime.now())
                .build();
    }

    public static RequestsResponse requestToResponse(Requests requests, List<Item> items) {
        return RequestsResponse.builder()
                .id(requests.getId())
                .creationDate(requests.getCreationDate())
                .description(requests.getDescription())
                .owner(requests.getOwner())
                .items(items)
                .build();
    }
}
