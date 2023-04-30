package ru.practicum.shareit.item.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "items")
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
@Setter
@Getter
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "owner")
    private Long owner;
    @Column(name = "is_available")
    private boolean available;
    @Column(name = "last_booking")
    private Long lastBooking;
    @Column(name = "next_booking")
    private Long nextBooking;
    @Column(name = "request")
    @JsonProperty(value = "requestId")
    private Long request;
}
