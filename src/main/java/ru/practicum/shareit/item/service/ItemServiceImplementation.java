package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemBadRequestException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.util.CommentMapper;
import ru.practicum.shareit.item.util.ItemMapper;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImplementation implements ItemService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserService userService;

    @Override
    public Comment createComment(CommentDto dto, long authorId, long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException(itemId));
        User user = userService.getUserById(authorId);
        return commentRepository.save(CommentMapper.dtoToComment(dto, item, user));
    }

    @Override
    public Item createItem(long ownerId, ItemDto itemDto) {
        if (itemDto.getAvailable() == null) throw new ItemBadRequestException("Missed available");
        if (itemDto.getName() == null || itemDto.getName().isBlank()) throw new ItemBadRequestException("Missed name");
        if (itemDto.getDescription() == null || itemDto.getDescription().isBlank()) throw new ItemBadRequestException("Missed description");
        if (userService.getUserById(ownerId) == null) throw new UserNotFoundException(ownerId);
        Item item = ItemMapper.dtoToItem(ownerId, itemDto);
        return itemRepository.save(item);
    }

    @Override
    public Stream<Item> getAllItemsByOwnerId(long ownerId) {
        return itemRepository.findAllByOwnerOrderByIdAsc(ownerId).stream();
    }

    @Override
    public Item getItemById(long itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isPresent()) {
            return item.get();
        } else {
            throw new ItemNotFoundException(itemId);
        }
    }

    @Override
    public Item updateItem(long ownerId, long itemId, ItemDto itemDto) {
        Item updatedItem = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException(itemId));
        if (updatedItem.getOwner() != ownerId) throw new ItemNotFoundException(ownerId);
        if (itemDto.getDescription() != null) updatedItem.setDescription(itemDto.getDescription());
        if (itemDto.getName() != null) updatedItem.setName(itemDto.getName());
        if (itemDto.getAvailable() != null) updatedItem.setAvailable(itemDto.getAvailable());
        return itemRepository.save(updatedItem);
    }

    @Override
    public void removeItem(long ownerId, long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException(itemId));
        if (item.getOwner() == ownerId) {
            itemRepository.deleteById(itemId);
        }
    }

    @Override
    public Stream<Item> searchItems(String text) {
        if (text.isBlank()) {
            return Stream.empty();
        }
        return itemRepository.findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text).stream();
    }
}
