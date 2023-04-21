package ru.practicum.shareit.RequestsTests;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.requests.model.Requests;
import ru.practicum.shareit.requests.repository.RequestsRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-request.sql" })
public class RequestsRepositoryTests {
    @Autowired
    private RequestsRepository requestsRepository;

    @Test
    void findAll() {
        List<Requests> requests = requestsRepository.findAll();
        Assertions.assertThat(requests).hasSize(2);
    }

    @Test
    void getUserById() {
        Optional<Requests> request = requestsRepository.findById(1L);
        Assertions.assertThat(request).hasValueSatisfying(f -> {
                    assertThat(f).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(f).hasFieldOrPropertyWithValue("description", "description");
                }
        );
    }

    @Test
    void checkUserExisting() {
        boolean existing = requestsRepository.existsById(1L);
        assertThat(existing).isTrue();
    }

    @Test
    void findAllByOwnerId() {
        List<Requests> requests = requestsRepository.findAllByOwner(1L);
        assertThat(requests).hasSize(1);
    }
}
