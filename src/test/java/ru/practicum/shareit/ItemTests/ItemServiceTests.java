package ru.practicum.shareit.ItemTests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import ru.practicum.shareit.booking.dto.BookingDtoToItem;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemBadRequestException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceTests {
    private final ItemService itemService;

    @Test
    @Sql(value = {"/test-schema.sql", "/test-create-user.sql"})
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
    @Sql(value = {"/test-schema.sql", "/test-create-user.sql"})
    void createItemMissingAvailableTest() {
        ItemDto itemDto = ItemDto.builder()
                .name("item")
                .description("item")
                .build();

        assertThrows(ItemBadRequestException.class, () -> itemService.createItem(1L, itemDto));
    }

    @Test
    @Sql(value = {"/test-schema.sql", "/test-create-user.sql"})
    void createItemMissingNameTest() {
        ItemDto itemDto = ItemDto.builder()
                .available(true)
                .description("item")
                .build();

        assertThrows(ItemBadRequestException.class, () -> itemService.createItem(1L, itemDto));
    }

    @Test
    @Sql(value = {"/test-schema.sql", "/test-create-user.sql"})
    void createItemBlankNameTest() {
        ItemDto itemDto = ItemDto.builder()
                .name("")
                .available(true)
                .description("item")
                .build();

        assertThrows(ItemBadRequestException.class, () -> itemService.createItem(1L, itemDto));
    }

    @Test
    @Sql(value = {"/test-schema.sql", "/test-create-user.sql"})
    void createItemMissingDescriptionTest() {
        ItemDto itemDto = ItemDto.builder()
                .name("name")
                .available(true)
                .build();

        assertThrows(ItemBadRequestException.class, () -> itemService.createItem(1L, itemDto));
    }

    @Test
    @Sql(value = {"/test-schema.sql", "/test-create-user.sql"})
    void createItemBlankDescriptionTest() {
        ItemDto itemDto = ItemDto.builder()
                .name("name")
                .available(true)
                .description("")
                .build();

        assertThrows(ItemBadRequestException.class, () -> itemService.createItem(1L, itemDto));
    }

    @Test
    @Sql(value = {"/test-schema.sql"})
    void createItemWrongUserTest() {
        ItemDto itemDto = ItemDto.builder()
                .name("name")
                .available(true)
                .description("description")
                .build();

        assertThrows(UserNotFoundException.class, () -> itemService.createItem(1L, itemDto));
    }

    @Test
    @Sql(value = {"/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql"})
    void getAllByOwnerIdWithCommentsTest() {

        List<ItemDto> items = itemService.getAllItemsByOwnerId(1L);

        Assertions.assertThat(items)
                .hasSize(1);

        Assertions.assertThat(Optional.of(items.get(0)))
                .isPresent()
                .hasValueSatisfying(i -> {
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("id", 1L);
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("name", "item");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("description", "description");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("available", true);
                    Assertions.assertThat(i).hasFieldOrProperty("lastBooking");
                    Assertions.assertThat(i).hasFieldOrProperty("nextBooking");
                    Assertions.assertThat(i.getNextBooking()).isNull();
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("comments", Collections.emptyList());
                });
    }

    @Test
    @Sql(value = {"/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql"})
    void getAllByOwnerIdWithBookingsTest() {

        List<ItemDto> items = itemService.getAllItemsByOwnerId(2L);

        Assertions.assertThat(items)
                .hasSize(1);

        ItemDto item = items.get(0);
        item.setLastBooking(new BookingDtoToItem());
        item.setNextBooking(new BookingDtoToItem());

        Assertions.assertThat(Optional.of(item))
                .isPresent()
                .hasValueSatisfying(i -> {
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("id", 2L);
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("name", "item 2");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("description", "description");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("available", false);
                    Assertions.assertThat(i).hasFieldOrProperty("lastBooking");
                    Assertions.assertThat(i).hasFieldOrProperty("nextBooking");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("comments", Collections.emptyList());
                });
    }

    @Test
    @Sql(value = {"/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql"})
    void getAllByOwnerIdTest() {

        List<ItemDto> items = itemService.getAllItemsByOwnerId(2L);

        Assertions.assertThat(items)
                .hasSize(1);

        ItemDto item = items.get(0);

        Assertions.assertThat(Optional.of(item))
                .isPresent()
                .hasValueSatisfying(i -> {
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("id", 2L);
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("name", "item 2");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("description", "description");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("available", false);
                    Assertions.assertThat(i).hasFieldOrProperty("lastBooking");
                    Assertions.assertThat(i).hasFieldOrProperty("nextBooking");
                    Assertions.assertThat(i.getNextBooking()).isNull();
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("comments", Collections.emptyList());
                });
    }

    @Test
    @Sql(value = {"/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql"})
    void getByOwnerIdWithCommentsTest() {
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
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("comments", Collections.emptyList());
                });
    }

    @Test
    @Sql(value = {"/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql"})
    void getByIdTest() {
        Optional<ItemDto> itemDto = Optional.of(itemService.getItemById(1L, 2L));

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
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("comments", Collections.emptyList());
                });
    }

    @Test
    @Sql(value = {"/test-schema.sql", "/test-create-user.sql"})
    void getByIdWrongItemTest() {
        assertThrows(ItemNotFoundException.class, () -> itemService.getItemById(1L, 2L));
    }

    @Test
    @Sql(value = {"/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql"})
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
    @Sql(value = {"/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql"})
    void updateItemWrongItemTest() {
        ItemDto itemDto = ItemDto.builder()
                .available(true)
                .build();
        assertThrows(ItemNotFoundException.class, () -> itemService.updateItem(1L, 11L, itemDto));
    }

    @Test
    @Sql(value = {"/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql"})
    void updateItemWrongOwnerTest() {
        ItemDto itemDto = ItemDto.builder()
                .available(true)
                .build();
        assertThrows(ItemNotFoundException.class, () -> itemService.updateItem(2L, 1L, itemDto));
    }

    @Test
    @Sql(value = {"/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql"})
    void updateItemMissingFieldsTest() {
        ItemDto itemDto = ItemDto.builder()
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
    @Sql(value = {"/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql"})
    void getAllByTextTest() {
        List<ItemDto> items = itemService.searchItems("item");

        Assertions.assertThat(items)
                .hasSize(1);

        Assertions.assertThat(items.get(0))
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @Sql(value = {"/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql"})
    void getAllByBlankTextTest() {
        List<ItemDto> items = itemService.searchItems("");

        Assertions.assertThat(items)
                .hasSize(0);
    }

    @Test
    @Sql(value = {"/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql", "/test-create-request.sql"})
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
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("id", 3L);
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("name", "item");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("description", "user 3 item");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("available", true);
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("requestId", 1L);
                });
    }

    @Test
    @Sql(value = {"/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql", "/test-create-booking.sql"})
    void addCommentTest() {
        CommentDto comment = CommentDto.builder()
                .authorName("author")
                .text("text")
                .build();

        Optional<CommentDto> commentDto = Optional.of(itemService.createComment(comment, 3L, 1L));

        Assertions.assertThat(commentDto)
                .isPresent()
                .hasValueSatisfying(i -> {
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("id", 1L);
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("authorName", "thirdUser");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("text", "text");
                });
    }

    @Test
    @Sql(value = {"/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql"})
    void removeItemTest() {
        itemService.removeItem(1L, 1L);

        List<ItemDto> items = itemService.getAllItemsByOwnerId(1L);

        Assertions.assertThat(items)
                .hasSize(0);
    }

    @Test
    @Sql(value = {"/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql"})
    void removeWrongItemTest() {
        assertThrows(ItemNotFoundException.class, () -> itemService.removeItem(1L, 10L));
    }

    @Test
    @Sql(value = {"/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql"})
    void removeItemWrongUserTest() {
        itemService.removeItem(2L, 1L);
    }
}
