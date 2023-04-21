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
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.util.BookingMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.DTO.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.util.UserMapper;

import java.time.LocalDateTime;
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
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingService bookingService;
    private final BookingRepository bookingRepository;

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
    @Sql(value = {"/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql"})
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
    @Sql(value = {"/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql"})
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
    void getAllByOwnerIdTest() {
        List<ItemDto> items = itemService.getAllItemsByOwnerId(1L);

        Assertions.assertThat(items)
                .hasSize(1);
        Assertions.assertThat(items.get(0))
                .hasFieldOrPropertyWithValue("name", "item");

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
    @Sql(value = {"/test-schema.sql", "/test-create-user.sql"})
    void addCommentTest() {
        UserDto userDto = UserDto.builder()
                .email("newuser@gmail.com")
                .name("user3")
                .build();

        User user = userService.createUser(UserMapper.toUser(userDto));

        ItemDto itemDto = ItemDto.builder()
                .description("item description")
                .name("item name")
                .available(true)
                .build();

        itemService.createItem(1L, itemDto);

        BookingDto bookingDto = BookingDto.builder()
                .start(LocalDateTime.now().minusYears(2))
                .end(LocalDateTime.now().minusYears(1))
                .itemId(1L)
                .build();

        bookingRepository.save(BookingMapper.dtoToBooking(bookingDto, user, itemRepository.findById(1L).orElseThrow(() -> new ItemNotFoundException(1))));

        bookingService.updateBooking(1L, 1L, true);

        CommentDto comment = CommentDto.builder()
                .authorName("author")
                .text("text")
                .build();

        Optional<CommentDto> commentDto = Optional.of(itemService.createComment(comment, 3L, 1L));

        Assertions.assertThat(commentDto)
                .isPresent()
                .hasValueSatisfying(i -> {
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("id", 1L);
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("authorName", "user3");
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

}
