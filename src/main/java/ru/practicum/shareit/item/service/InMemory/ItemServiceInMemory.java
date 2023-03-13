package ru.practicum.shareit.item.service.InMemory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.BadRequestException;
import ru.practicum.shareit.util.NotFoundException;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

@Service

public class ItemServiceInMemory implements ItemService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    UserService userService;

    @Override
    public Item createItem(int ownerId, ItemDto item) {
        if (item.getAvailable() == null) {
            throw new BadRequestException("Missed available");
        }
        if (item.getName() == null || item.getName().isBlank()) {
            throw new BadRequestException("Missed name");
        }
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            throw new BadRequestException("Missed description");
        }
        return itemRepository.save(ownerId, item);
    }

    @Override
    public Stream<Item> getAllItemsByOwnerId(int ownerId) {
        return itemRepository.getAll(ownerId);
    }

    @Override
    public Item getItemById(int itemId) {
        if (itemRepository.getById(itemId).isPresent()) {
            return itemRepository.getById(itemId).get();
        } else {
            throw new NotFoundException("Item not found");
        }
    }

    @Override
    public Item updateItem(int ownerId, int itemId, ItemDto itemDto) {
        if (itemRepository.getById(itemId).isPresent()) {
            Item updatedItem = itemRepository.getById(itemId).get();
            if (updatedItem.getOwnerId() != ownerId) {
                throw new NotFoundException("item not found");
            }
            if (itemDto.getDescription() != null) {
                updatedItem.setDescription(itemDto.getDescription());
            }
            if (itemDto.getName() != null) {
                updatedItem.setName(itemDto.getName());
            }
            if (itemDto.getAvailable() != null) {
                updatedItem.setAvailable(itemDto.getAvailable());
            }
            return itemRepository.update(updatedItem);
        } else {
            throw new NotFoundException("Item not found");
        }
    }

    @Override
    public void removeItem(int ownerId, int itemId) {
        Optional<Item> item = itemRepository.getById(itemId);
        if (item.isPresent()) {
            if (item.get().getOwnerId() == ownerId) {
                itemRepository.delete(itemId);
            }
        } else {
            throw new NotFoundException("item not found");
        }
    }

    @Override
    public Stream<Item> searchItems(String text) {
        if (text.isBlank()) {
            return Collections.EMPTY_LIST.stream();
        }
        return itemRepository.searchItem(text);
    }
}
