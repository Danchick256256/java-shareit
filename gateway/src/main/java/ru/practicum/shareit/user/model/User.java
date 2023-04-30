package ru.practicum.shareit.user.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "users")
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
@Setter
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;
    @Column(name = "name")
    @JsonProperty("name")
    private String name;
    @Column(name = "email")
    @JsonProperty("email")
    private String email;
}
