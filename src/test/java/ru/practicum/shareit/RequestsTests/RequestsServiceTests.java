package ru.practicum.shareit.RequestsTests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import ru.practicum.shareit.requests.exception.RequestsBadRequestException;
import ru.practicum.shareit.requests.service.RequestsService;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
class RequestsServiceTests {
    private final RequestsService requestService;
    private final UserService userService;


    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql" })
    void createRequestTest() {
        long userId = 1L;
        RequestsDto incomeDto = RequestsDto.builder()
                .description("text")
                .build();

        Optional<RequestsResponse> requestsResponse = Optional.of(requestService.createRequest(incomeDto, userId));

        assertThat(requestsResponse)
                .isPresent()
                .hasValueSatisfying(i -> {
                    assertThat(i).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(i).hasFieldOrPropertyWithValue("description", "text");
                    assertThat(i).hasFieldOrProperty("creationDate");
                    assertThat(i.getCreationDate()).isNotNull();
                    assertThat(i).hasFieldOrProperty("owner");
                });
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql" })
    void createRequestWithoutDescriptionTest() {
        long userId = 1L;
        RequestsDto incomeDto = RequestsDto.builder()
                .build();

        assertThrows(RequestsBadRequestException.class, () -> requestService.createRequest(incomeDto, userId));
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql" })
    void createRequestWithBlankDescriptionTest() {
        long userId = 1L;
        RequestsDto incomeDto = RequestsDto.builder()
                .description("")
                .build();

        assertThrows(RequestsBadRequestException.class, () -> requestService.createRequest(incomeDto, userId));
    }

    @Test
    @Sql(value = { "/test-schema.sql" })
    void createRequestWithoutUserTest() {
        long userId = 1L;
        RequestsDto incomeDto = RequestsDto.builder()
                .description("text")
                .build();

        assertThrows(UserNotFoundException.class, () -> requestService.createRequest(incomeDto, userId));
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-request.sql" })
    void getByIdTest() {
        long requestId = 1L;
        long userId = 1L;

        RequestsDto incomeDto = RequestsDto.builder()
                .description("text")
                .build();

        requestService.createRequest(incomeDto, userId);

        Optional<RequestsResponse> requestsResponse = Optional.of(requestService.getRequestById(requestId, userId));

        assertThat(requestsResponse)
                .isPresent()
                .hasValueSatisfying(i -> {
                    assertThat(i).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(i).hasFieldOrPropertyWithValue("description", "description");
                    assertThat(i).hasFieldOrProperty("creationDate");
                    assertThat(i.getCreationDate()).isNotNull();
                    assertThat(i).hasFieldOrProperty("items");
                    assertThat(i.getItems()).hasSize(0);
                });
    }

    @Test
    @Sql(value = { "/test-schema.sql" })
    void getByIdWithoutUserTest() {
        long requestId = 1L;
        long userId = 1L;

        assertThrows(UserNotFoundException.class, () -> requestService.getRequestById(requestId, userId));
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-request.sql" })
    void getRequestByOwnerIdTest() {
        long userId = 1L;

        List<RequestsResponse> requestsResponseList = requestService.getRequestByOwnerId(userId);
        assertThat(requestsResponseList)
                .hasSize(1);
        Optional<RequestsResponse> requestsResponse = Optional.of(requestsResponseList.get(0));
        assertThat(requestsResponse)
                .isPresent()
                .hasValueSatisfying(i -> {
                    assertThat(i).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(i).hasFieldOrPropertyWithValue("description", "description");
                    assertThat(i).hasFieldOrProperty("creationDate");
                    assertThat(i.getCreationDate()).isNotNull();
                    assertThat(i).hasFieldOrProperty("items");
                    assertThat(i.getItems()).hasSize(0);
                });
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-request.sql" })
    void getRequestByWrongOwnerIdTest() {
        long userId = 3L;

        List<RequestsResponse> requestsResponseList = requestService.getRequestByOwnerId(userId);
        assertThat(requestsResponseList)
                .hasSize(0);
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-request.sql" })
    void getRequestByUnknownOwnerIdTest() {
        long userId = 20L;

        assertThrows(UserNotFoundException.class, () -> requestService.getRequestByOwnerId(userId));
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-request.sql" })
    void getAllRequestsTest() {
        long userId = 1L;

        List<RequestsResponse> requestsResponseList = requestService.getAllRequests(userId, 0L, 10L);
        assertThat(requestsResponseList)
                .hasSize(1);
        Optional<RequestsResponse> requestsResponse = Optional.of(requestsResponseList.get(0));
        assertThat(requestsResponse)
                .isPresent()
                .hasValueSatisfying(i -> {
                    assertThat(i).hasFieldOrPropertyWithValue("id", 2L);
                    assertThat(i).hasFieldOrPropertyWithValue("description", "description");
                    assertThat(i).hasFieldOrProperty("creationDate");
                    assertThat(i.getCreationDate()).isNotNull();
                    assertThat(i).hasFieldOrProperty("items");
                    assertThat(i.getItems()).hasSize(0);
                });
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-request.sql" })
    void getAllRequestsZeroSizeTest() {
        long userId = 1L;

        assertThrows(RequestsBadRequestException.class, () -> requestService.getAllRequests(userId, 0L, 0L));
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-request.sql" })
    void getAllRequestsWrongSizeTest() {
        long userId = 1L;

        assertThrows(RequestsBadRequestException.class, () -> requestService.getAllRequests(userId, -100L, -10L));
    }

    @Test
    @Sql(value = { "/test-schema.sql", "/test-create-user.sql", "/test-create-request.sql" })
    void deleteRequestTest() {
        long userId = 1L;

        requestService.deleteRequest(1);

        List<RequestsResponse> requestsResponseList = requestService.getAllRequests(userId, 1L, 10L);
        assertThat(requestsResponseList)
                .hasSize(0);
    }
}
