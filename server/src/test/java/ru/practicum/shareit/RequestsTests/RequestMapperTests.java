package ru.practicum.shareit.RequestsTests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import ru.practicum.shareit.requests.dto.RequestsDto;
import ru.practicum.shareit.requests.dto.RequestsResponse;
import ru.practicum.shareit.requests.model.Requests;
import ru.practicum.shareit.requests.util.RequestMapper;

import java.time.LocalDateTime;
import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
class RequestMapperTests {
    @Test
    @Sql(value = {"/test-schema.sql", "/test-create-user.sql" })
    void dtoToRequestTest() {
        RequestsDto requestsDto = RequestsDto.builder()
                .description("description")
                .build();

        Requests requests = RequestMapper.dtoToRequest(requestsDto, 1L);

        Assertions.assertThat(requests)
                .hasFieldOrProperty("creationDate")
                .hasFieldOrPropertyWithValue("owner", 1L)
                .hasFieldOrPropertyWithValue("description", "description");
    }

    @Test
    @Sql(value = {"/test-schema.sql", "/test-create-user.sql" })
    void requestToResponseTest() {
        Requests requests = Requests.builder()
                .id(1L)
                .creationDate(LocalDateTime.now())
                .owner(1L)
                .description("description")
                .build();

        RequestsResponse requestsResponse = RequestMapper.requestToResponse(requests, Collections.emptyList());

        Assertions.assertThat(requestsResponse)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrProperty("creationDate")
                .hasFieldOrPropertyWithValue("owner", 1L)
                .hasFieldOrPropertyWithValue("description", "description")
                .hasFieldOrPropertyWithValue("items", Collections.emptyList());
    }
}
