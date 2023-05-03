package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.model.User;

@Service
public class UserClient extends BaseClient {
    @Autowired
    public UserClient(@Value("${server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                    .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + "/users"))
                    .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                    .build()
        );
    }

    public ResponseEntity<Object> addUser(User user) {
        return post("", null, null, user);
    }

    public ResponseEntity<Object> getAll() {
        return get("");
    }

    public ResponseEntity<Object> getById(long userId) {
        return get("/" + userId, null, null);
    }

    public ResponseEntity<Object> update(long userId, User user) {
        return patch("/" + userId, null, null, user);
    }

    public void delete(long userId) {
        delete("/" + userId);
    }
}
