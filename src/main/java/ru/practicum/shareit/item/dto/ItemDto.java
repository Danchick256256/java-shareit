package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import ru.practicum.shareit.booking.dto.BookingDtoToItem;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@Getter
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Long ownerId;
    private Boolean available;
    private BookingDtoToItem lastBooking;
    private BookingDtoToItem nextBooking;
    private List<CommentDto> comments;
}
