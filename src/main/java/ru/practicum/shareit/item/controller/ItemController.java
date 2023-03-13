package ru.practicum.shareit.item.controller;

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
public class ItemController {
    @Autowired
    private ItemService itemService;

    @GetMapping
    public List<Item> getAllItems(@RequestHeader("X-Sharer-User-Id") int ownerId) {
        return itemService.getAllItemsByOwnerId(ownerId).collect(Collectors.toList());
    }

    @GetMapping("/{itemId}")
    public Item getItemById(@PathVariable int itemId) {
        return itemService.getItemById(itemId);
    }

    @PostMapping
    public Item createItem(@RequestHeader("X-Sharer-User-Id") int ownerId, @RequestBody ItemDto itemDto) {
        return itemService.createItem(ownerId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@RequestHeader("X-Sharer-User-Id") int ownerId, @PathVariable int itemId, @RequestBody ItemDto itemDto) {
        return itemService.updateItem(ownerId, itemId, itemDto);
    }

    @DeleteMapping
    public void removeItem(@RequestHeader("X-Sharer-User-Id") int ownerId, @RequestBody int itemId) {
        itemService.removeItem(ownerId, itemId);
    }

    @GetMapping("/search")
    public List<Item> searchItem(@RequestHeader("X-Sharer-User-Id") int ownerId, @RequestParam String text) {
        return itemService.searchItems(text).collect(Collectors.toList());
    }

    @ExceptionHandler({ NotFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> notFoundExceptionHandler() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ BadRequestException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> badRequestExceptionHandler() {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
