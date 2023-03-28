package ru.practicum.shareit.item.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {
    @Autowired
    private ItemService itemService;
    private final String userHeader = "X-Sharer-User-Id";

    @GetMapping
    public List<Item> getAllItems(@RequestHeader(userHeader) int ownerId) {
        log.info("get all items by owner id = {}", ownerId);
        return itemService.getAllItemsByOwnerId(ownerId).collect(Collectors.toList());
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable int itemId) {
        log.info("get item by id = {}", itemId);
        return itemService.getItemById(itemId);
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader(userHeader) int ownerId, @RequestBody ItemDto itemDto) {
        log.info("create item by owner id = {}, data = {}", ownerId, itemDto.toString());
        return itemService.createItem(ownerId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@RequestHeader(userHeader) int ownerId, @PathVariable int itemId, @RequestBody ItemDto itemDto) {
        log.info("update item by owner id = {}, item id = {}, data = {}", ownerId, itemId, itemDto);
        return itemService.updateItem(ownerId, itemId, itemDto);
    }

    @DeleteMapping
    public void removeItem(@RequestHeader(userHeader) int ownerId, @RequestBody int itemId) {
        log.info("delete item by owner id = {}, item id = {}", ownerId, itemId);
        itemService.removeItem(ownerId, itemId);
    }

    @GetMapping("/search")
    public List<Item> searchItem(@RequestParam String text) {
        log.info("search item by text = {}", text);
        return itemService.searchItems(text).collect(Collectors.toList());
    }

    @PostMapping("{itemId}/comment")
    public CommentDto addComment(@Valid @RequestBody CommentDto dto, @RequestHeader(userHeader) long author,
                              @PathVariable long itemId) {
        log.info("create comment by author id = {}, item id = {}, data = {}", author, itemId, dto.toString());
        return itemService.createComment(dto, author, itemId);
    }
}
