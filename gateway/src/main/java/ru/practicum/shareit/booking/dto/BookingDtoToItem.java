package ru.practicum.shareit.booking.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class BookingDtoToItem {
    private Long id;
    private Long bookerId;
}
