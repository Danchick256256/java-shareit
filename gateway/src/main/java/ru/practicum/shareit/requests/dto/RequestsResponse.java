package ru.practicum.shareit.requests.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
@Setter
@Getter
public class RequestsResponse {
    private Long id;
    @JsonProperty(value = "created")
    private LocalDateTime creationDate;
    private String description;
    private Long owner;
    private List<Item> items;
}
