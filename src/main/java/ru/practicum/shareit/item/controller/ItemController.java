package ru.practicum.shareit.item.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.util.BadRequestException;
import ru.practicum.shareit.util.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {
    @Autowired
    private ItemService itemService;
    
    private final String UserHeader = "X-Sharer-User-Id";

    @GetMapping
    public List<Item> getAllItems(@RequestHeader(UserHeader) int ownerId) {
        log.info("get.all.items.request");
        return itemService.getAllItemsByOwnerId(ownerId).collect(Collectors.toList());
    }

    @GetMapping("/{itemId}")
    public Item getItemById(@PathVariable int itemId) {
        log.info("get.item.by.id.request");
        return itemService.getItemById(itemId);
    }

    @PostMapping
    public Item createItem(@RequestHeader(UserHeader) int ownerId, @RequestBody ItemDto itemDto) {
        log.info("create.item.request");
        return itemService.createItem(ownerId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@RequestHeader(UserHeader) int ownerId, @PathVariable int itemId, @RequestBody ItemDto itemDto) {
        log.info("update.item.request");
        return itemService.updateItem(ownerId, itemId, itemDto);
    }

    @DeleteMapping
    public void removeItem(@RequestHeader(UserHeader) int ownerId, @RequestBody int itemId) {
        log.info("remove.item.request");
        itemService.removeItem(ownerId, itemId);
    }

    @GetMapping("/search")
    public List<Item> searchItem(@RequestHeader(UserHeader) int ownerId, @RequestParam String text) {
        log.info("search.item.request");
        return itemService.searchItems(text).collect(Collectors.toList());
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> notFoundExceptionHandler() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> badRequestExceptionHandler() {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
