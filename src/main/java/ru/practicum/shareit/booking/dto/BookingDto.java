package ru.practicum.shareit.booking.dto;

import lombok.*;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.sql.Timestamp;
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
