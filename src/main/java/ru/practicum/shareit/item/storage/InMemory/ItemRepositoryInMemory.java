package ru.practicum.shareit.item.storage.InMemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.item.util.ItemMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.NotFoundException;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
public class ItemRepositoryInMemory implements ItemRepository {

    @Autowired
    UserService userService;
    ArrayList<Item> itemList;

    public ItemRepositoryInMemory() {
        itemList = new ArrayList<>();
    }

    @Override
    public Item save(int ownerId, ItemDto item) {
        if (userService.getAllUsers().stream().map(User::getId).collect(Collectors.toList()).contains(ownerId)) {
            Item mappedItem = ItemMapper.dtoToItem(ownerId, item);
            itemList.add(mappedItem);
            return itemList.stream().filter(it -> it.getId() == mappedItem.getId()).findFirst().get();
        } else {
            throw new NotFoundException("User not found");
        }
    }

    @Override
    public Stream<Item> getAll(int ownerId) {
        return itemList.stream().filter(item -> item.getOwnerId() == ownerId);
    }

    @Override
    public Optional<Item> getById(int id) {
        return itemList.stream().filter(it -> it.getId() == id).findFirst();
    }

    @Override
    public Item update(Item item) {
        itemList.remove(itemList.stream().filter(it -> it.getId() == item.getId()).findFirst().get());
        itemList.add(item);
        return itemList.stream().filter(it -> it.getId() == item.getId()).findFirst().get();
    }

    @Override
    public void delete(int id) {
        itemList.remove(itemList.stream().filter(it -> it.getId() == id).findFirst().get());
    }

    @Override
    public Stream<Item> searchItem(String text) {
        String formattedText = text.toLowerCase();
        return itemList
                .stream()
                .filter(item -> item.getName().toLowerCase().contains(formattedText)
                        || item.getDescription().toLowerCase().contains(formattedText)
                        && item.isAvailable());
    }
}
