package ru.practicum.shareit.booking.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class BookingDto {
    private Long itemId;
    //@Past
    @NotNull
    private LocalDateTime start;
    //@FutureOrPresent
    @NotNull
    private LocalDateTime end;
}
