package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.constants.Constants;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getAllItems(@RequestHeader(Constants.userHeader) int ownerId) {
        log.info("get all items by owner id = {}", ownerId);
        return itemClient.getAll(ownerId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable int itemId, @RequestHeader(Constants.userHeader) int ownerId) {
        log.info("get item by id = {}, user id = {}", itemId, ownerId);
        return itemClient.getById(itemId, ownerId);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(Constants.userHeader) int ownerId, @RequestBody ItemDto itemDto) {
        log.info("create item by owner id = {}, data = {}", ownerId, itemDto.toString());
        return itemClient.addItem(ownerId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(Constants.userHeader) int ownerId, @PathVariable int itemId, @RequestBody ItemDto itemDto) {
        log.info("update item by owner id = {}, item id = {}, data = {}", ownerId, itemId, itemDto);
        return itemClient.update(ownerId, itemId, itemDto);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestHeader(Constants.userHeader) int ownerId, @RequestParam String text) {
        log.info("search item by text = {}", text);
        return itemClient.search(ownerId, text);
    }

    @PostMapping("{itemId}/comment")
    public ResponseEntity<Object> addComment(@Valid @RequestBody CommentDto dto, @RequestHeader(Constants.userHeader) long author,
                                             @PathVariable long itemId) {
        log.info("create comment by author id = {}, item id = {}, data = {}", author, itemId, dto.toString());
        return itemClient.addComment(author, itemId, dto);
    }
}
