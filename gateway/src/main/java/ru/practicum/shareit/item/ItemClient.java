package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    @Autowired
    public ItemClient(@Value("${server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                    .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + "/items"))
                    .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                    .build()
        );
    }

    public ResponseEntity<Object> addItem(long userId, ItemDto itemDto) {
        return post("", userId, null, itemDto);
    }

    public ResponseEntity<Object> getAll(long userId) {
        return get("", userId, null);
    }

    public ResponseEntity<Object> getById(long itemId, long userId) {
        return get("/" + itemId, userId, null);
    }

    public ResponseEntity<Object> update(long userId, long itemId, ItemDto itemDto) {
        return patch("/" + itemId, userId, null, itemDto);
    }

    public ResponseEntity<Object> search(long userId, String text) {
        return get("/search?text=" + text, userId, null);
    }

    public ResponseEntity<Object> addComment(long userId, long itemId, CommentDto commentDto) {
        return post("/" + itemId + "/comment", userId, null, commentDto);
    }
}
