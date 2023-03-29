package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class BookingDtoResponse {
    @JsonProperty("id")
    private Long id;
    private User booker;
    private Item item;
    private String start;
    private String end;
    private BookingState status;

}
