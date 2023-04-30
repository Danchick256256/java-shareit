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
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.util.ItemMapper;

import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
class ItemMapperTests {
    private final ItemRepository itemRepository;

    @Test
    @Sql(value = {"/test-schema.sql", "/test-create-user.sql" })
    void dtoToItemTest() {
        ItemDto itemDto = ItemDto.builder()
                .name("item")
                .description("description")
                .available(true)
                .build();

        Item item = ItemMapper.dtoToItem(1L, itemDto);

        Assertions.assertThat(item)
                .hasFieldOrPropertyWithValue("available", true)
                .hasFieldOrPropertyWithValue("owner", 1L)
                .hasFieldOrPropertyWithValue("name", "item")
                .hasFieldOrPropertyWithValue("description", "description");
    }

    @Test
    @Sql(value = {"/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql" })
    void itemToDtoTest() {
        ItemDto itemDto = ItemMapper.itemToDto(itemRepository.findById(1L).get(), null, null, Collections.emptyList());

        Assertions.assertThat(itemDto)
                .hasFieldOrPropertyWithValue("available", true)
                .hasFieldOrPropertyWithValue("ownerId", 1L)
                .hasFieldOrPropertyWithValue("name", "item")
                .hasFieldOrPropertyWithValue("description", "description")
                .hasFieldOrPropertyWithValue("lastBooking", null)
                .hasFieldOrPropertyWithValue("nextBooking", null)
                .hasFieldOrPropertyWithValue("requestId", 1L)
                .hasFieldOrPropertyWithValue("comments", Collections.emptyList());
    }
}
