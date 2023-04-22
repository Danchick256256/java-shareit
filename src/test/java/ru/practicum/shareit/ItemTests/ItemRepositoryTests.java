package ru.practicum.shareit.ItemTests;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql" })
public class ItemRepositoryTests {
    @Autowired
    private ItemRepository itemRepository;

    @Test
    void findAll() {
        List<Item> items = itemRepository.findAll();
        Assertions.assertThat(items).hasSize(2);
    }

    @Test
    void getUserById() {
        Optional<Item> item = itemRepository.findById(1L);
        Assertions.assertThat(item).hasValueSatisfying(f -> {
                    assertThat(f).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(f).hasFieldOrPropertyWithValue("description", "description");
                    assertThat(f).hasFieldOrPropertyWithValue("available", true);
                    assertThat(f).hasFieldOrPropertyWithValue("name", "item");
                    assertThat(f).hasFieldOrProperty("owner");
                }
        );
    }

    @Test
    void checkUserExisting() {
        boolean existing = itemRepository.existsById(1L);
        assertThat(existing).isTrue();
    }

    @Test
    void searchItems() {
        List<Item> items = itemRepository.findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue("item", "item");
        assertThat(items).hasSize(1);
    }

    @Test
    void findAllByOwnerId() {
        List<Item> items = itemRepository.findAllByOwnerOrderByIdAsc(1L);
        assertThat(items).hasSize(1);
    }

    @Test
    void findAllByRequestId() {
        List<Item> items = itemRepository.findAllByRequestOrderByIdAsc(1L);
        assertThat(items).hasSize(2);
    }
}
