package ru.practicum.shareit.ItemTests;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
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
import ru.practicum.shareit.user.service.UserService;

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
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql" })
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
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql" })
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
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("description", "description");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("available", true);
                });
    }

    @Test
    void updateUnknownItemTest() {
        ItemDto itemDto = ItemDto.builder()
                .name("updated item")
                .description("updated item")
                .available(true)
                .build();

        assertThrows(ItemNotFoundException.class, () -> itemService.updateItem(10L, 1L, itemDto));

    }

    @Test
    void getByWrongIdTest() {
        assertThrows(ItemNotFoundException.class, () -> itemService.getItemById(100L, 1L));
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql" })
    void getByOwnerIdTest() {
        Optional<ItemDto> itemDto = Optional.of(itemService.getItemById(1L, 1L));

        Assertions.assertThat(itemDto)
                .isPresent()
                .hasValueSatisfying(i -> {
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("id", 1L);
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("name", "item");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("description", "description");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("available", true);
                    Assertions.assertThat(i).hasFieldOrProperty("lastBooking");
                    Assertions.assertThat(i).hasFieldOrProperty("nextBooking");
                    Assertions.assertThat(i.getNextBooking()).isNull();
                    Assertions.assertThat(i).hasFieldOrProperty("comments");
                });
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql" })
    void getByWrongOwnerId() {
        Optional<ItemDto> itemDto = Optional.of(itemService.getItemById(1L, 3L));

        Assertions.assertThat(itemDto)
                .isPresent()
                .hasValueSatisfying(i -> {
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("id", 1L);
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("name", "item");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("description", "description");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("available", true);
                    Assertions.assertThat(i.getLastBooking()).isNull();
                });
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql" })
    void getAllByTextTest() {
        getByOwnerIdTest();
        List<ItemDto> items = itemService.searchItems("item");

        Assertions.assertThat(items)
                .hasSize(1);

        Assertions.assertThat(items.get(0))
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql" })
    void getAllByOwnerIdTest() {
        getAllByTextTest();
        List<ItemDto> items = itemService.getAllItemsByOwnerId(1L);

        Assertions.assertThat(items)
                .hasSize(1);
        Assertions.assertThat(items.get(0))
                .hasFieldOrPropertyWithValue("name", "item");

    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql", "/test-create-request.sql" })
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
