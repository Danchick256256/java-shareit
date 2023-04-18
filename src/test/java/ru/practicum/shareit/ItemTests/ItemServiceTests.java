package ru.practicum.shareit.ItemTests;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.DTO.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.util.UserMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceTests {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;

    @Test
    @Order(0)
    @Sql(value = {"/test-schema.sql"})
    void createUsersTest() {
        UserDto userCreateDto = UserDto.builder()
                .name("user")
                .email("user@gmail.com")
                .build();
        userService.createUser(UserMapper.toUser(userCreateDto));
    }

    @Test
    @Order(1)
    void createItemTest() {
        ItemDto itemDto = ItemDto.builder()
                .name("item")
                .description("item")
                .available(false)
                .build();
        Optional<ItemDto> itemDtoResponse = Optional.of(itemService.createItem(1L, itemDto));

        Assertions.assertThat(itemDtoResponse)
                .isPresent()
                .hasValueSatisfying(i -> {
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("id", 1L);
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("name", "item");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("description", "item");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("available", false);
                });
    }

    @Test
    @Order(2)
    void updateAvailableItemTest() {
        ItemDto itemDto = ItemDto.builder()
                .available(true)
                .build();
        Optional<ItemDto> itemDtoResponse = Optional.of(itemService.updateItem(1L, 1L, itemDto));

        Assertions.assertThat(itemDtoResponse)
                .isPresent()
                .hasValueSatisfying(i -> {
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("id", 1L);
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("name", "item");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("description", "item");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("available", true);
                });
    }

    @Test
    @Order(3)
    void updateUnknownItemTest() {
        ItemDto itemDto = ItemDto.builder()
                .name("updated item")
                .description("updated item")
                .available(true)
                .build();

        assertThrows(ItemNotFoundException.class, () -> itemService.updateItem(10L, 1L, itemDto));

    }

    @Test
    @Order(4)
    void getByWrongIdTest() {
        assertThrows(ItemNotFoundException.class, () -> itemService.getItemById(100L, 1L));
    }

    @Test
    @Order(5)
    void getByOwnerIdTest() {
        Optional<ItemDto> itemDto = Optional.of(itemService.getItemById(1L, 1L));

        Assertions.assertThat(itemDto)
                .isPresent()
                .hasValueSatisfying(i -> {
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("id", 1L);
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("name", "item");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("description", "item");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("available", true);
                    Assertions.assertThat(i).hasFieldOrProperty("lastBooking");
                    Assertions.assertThat(i).hasFieldOrProperty("bookerId");
                    Assertions.assertThat(i).hasFieldOrProperty("nextBooking");
                    Assertions.assertThat(i.getNextBooking()).isNull();
                    Assertions.assertThat(i).hasFieldOrProperty("comments");
                    Assertions.assertThat(i.getComments()).hasSize(1);
                });
    }

    @Test
    @Order(6)
    void getByWrongOwnerId() {
        Optional<ItemDto> itemDto = Optional.of(itemService.getItemById(1L, 3L));

        Assertions.assertThat(itemDto)
                .isPresent()
                .hasValueSatisfying(i -> {
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("id", 1L);
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("name", "item");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("description", "item");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("available", true);
                    Assertions.assertThat(i.getLastBooking()).isNull();
                });
    }

    @Test
    @Order(7)
    void getAllByTextTest() {
        List<ItemDto> items = itemService.searchItems("item");

        Assertions.assertThat(items)
                .hasSize(1);

        Assertions.assertThat(items.get(0))
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @Order(8)
    void getAllByOwnerIdTest() {
        List<ItemDto> items = itemService.getAllItemsByOwnerId(1L);

        Assertions.assertThat(items)
                .hasSize(1);
        Assertions.assertThat(items.get(0))
                .hasFieldOrPropertyWithValue("name", "item");

        items = itemService.getAllItemsByOwnerId(1L);

        Assertions.assertThat(items)
                .hasSize(1);
        Assertions.assertThat(items.get(0))
                .hasFieldOrPropertyWithValue("name", "item");
    }

    @Test
    @Order(9)
    void createItemWithRequestTest() {
        ItemDto itemDto = ItemDto.builder()
                .name("item")
                .description("user 3 item")
                .available(true)
                .requestId(1L)
                .build();
        Optional<ItemDto> itemDtoResponse = Optional.of(itemService.createItem(1L, itemDto));

        Assertions.assertThat(itemDtoResponse)
                .isPresent()
                .hasValueSatisfying(i -> {
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("id", 2L);
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("name", "item");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("description", "user 3 item");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("available", true);
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("requestId", 1L);
                });
    }

}
