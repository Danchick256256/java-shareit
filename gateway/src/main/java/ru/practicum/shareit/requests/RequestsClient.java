package ru.practicum.shareit.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.requests.dto.RequestsDto;
import ru.practicum.shareit.user.model.User;

import java.util.Map;

@Service
public class RequestsClient extends BaseClient {
    @Autowired
    public RequestsClient(@Value("${server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                    .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + "/requests"))
                    .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                    .build()
        );
    }

    public ResponseEntity<Object> addRequest(long ownerId, RequestsDto requestsDto) {
        return post("", ownerId, null, requestsDto);
    }

    public ResponseEntity<Object> getAll(long userId) {
        return get("", userId, null);
    }

    public ResponseEntity<Object> getAllByOwnerId(long userId, long from, long size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("/all?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getById(long userId, long requestId) {
        return get("/" + requestId, userId, null);
    }

    public ResponseEntity<Object> update(long userId, User user) {
        return patch("/" + userId, null, null, user);
    }

    public ResponseEntity<Object> delete(long userId) {
        return patch("/" + userId, null, null, null);
    }
}
