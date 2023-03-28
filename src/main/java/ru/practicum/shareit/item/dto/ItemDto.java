package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
@Builder
@Getter
public class ItemDto {
    private String name;
    private String description;
    private Long ownerId;
    private Boolean available;
}
