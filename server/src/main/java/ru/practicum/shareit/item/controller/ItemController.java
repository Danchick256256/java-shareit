package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.constants.Constants;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> getAllItems(@RequestHeader(Constants.userHeader) int ownerId) {
        log.info("get all items by owner id = {}", ownerId);
        return itemService.getAllItemsByOwnerId(ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable int itemId, @RequestHeader(Constants.userHeader) int ownerId) {
        log.info("get item by id = {}, user id = {}", itemId, ownerId);
        return itemService.getItemById(itemId, ownerId);
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader(Constants.userHeader) int ownerId, @RequestBody ItemDto itemDto) {
        log.info("create item by owner id = {}, data = {}", ownerId, itemDto.toString());
        return itemService.createItem(ownerId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(Constants.userHeader) int ownerId, @PathVariable int itemId, @RequestBody ItemDto itemDto) {
        log.info("update item by owner id = {}, item id = {}, data = {}", ownerId, itemId, itemDto);
        return itemService.updateItem(ownerId, itemId, itemDto);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text) {
        log.info("search item by text = {}", text);
        return itemService.searchItems(text);
    }

    @PostMapping("{itemId}/comment")
    public CommentDto addComment(@RequestBody CommentDto dto, @RequestHeader(Constants.userHeader) long author,
                                 @PathVariable long itemId) {
        log.info("create comment by author id = {}, item id = {}, data = {}", author, itemId, dto.toString());
        return itemService.createComment(dto, author, itemId);
    }
}
