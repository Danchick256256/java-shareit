package ru.practicum.shareit.RequestsTests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import ru.practicum.shareit.requests.dto.RequestsDto;
import ru.practicum.shareit.requests.dto.RequestsResponse;
import ru.practicum.shareit.requests.service.RequestsService;
import ru.practicum.shareit.user.DTO.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.util.UserMapper;

import java.util.List;
import java.util.Optional;

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
    @Order(1)
    @Sql(value = { "/test-schema.sql" })
    void createTest() {
        UserDto userCreateDto = UserDto.builder()
                .name("user")
                .email("user@gmail.com")
                .build();
        userService.createUser(UserMapper.toUser(userCreateDto));

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
    @Order(2)
    void getByIdTest() {
        long requestId = 1L;
        long userId = 1L;

        UserDto userCreateDto = UserDto.builder()
                .name("user")
                .email("user@gmail.com")
                .build();
        userService.createUser(UserMapper.toUser(userCreateDto));

        RequestsDto incomeDto = RequestsDto.builder()
                .description("text")
                .build();

        requestService.createRequest(incomeDto, userId);

        Optional<RequestsResponse> requestsResponse = Optional.of(requestService.getRequestById(requestId, userId));

        assertThat(requestsResponse)
                .isPresent()
                .hasValueSatisfying(i -> {
                    assertThat(i).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(i).hasFieldOrPropertyWithValue("description", "text");
                    assertThat(i).hasFieldOrProperty("creationDate");
                    assertThat(i.getCreationDate()).isNotNull();
                    assertThat(i).hasFieldOrProperty("items");
                    assertThat(i.getItems()).hasSize(0);
                });
    }

    @Test
    @Order(3)
    void getForOwnerTest() {
        UserDto userCreateDto = UserDto.builder()
                .name("user")
                .email("11user@gmail.com")
                .build();
        userService.createUser(UserMapper.toUser(userCreateDto));

        RequestsDto incomeDto = RequestsDto.builder()
                .description("text")
                .build();

        requestService.createRequest(incomeDto, 1L);

        long userId = 1L;

        List<RequestsResponse> requestsResponseList = requestService.getRequestByOwnerId(userId);
        assertThat(requestsResponseList)
                .hasSize(1);
        Optional<RequestsResponse> requestsResponse = Optional.of(requestsResponseList.get(0));
        assertThat(requestsResponse)
                .isPresent()
                .hasValueSatisfying(i -> {
                    assertThat(i).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(i).hasFieldOrPropertyWithValue("description", "text");
                    assertThat(i).hasFieldOrProperty("creationDate");
                    assertThat(i.getCreationDate()).isNotNull();
                    assertThat(i).hasFieldOrProperty("items");
                    assertThat(i.getItems()).hasSize(0);
                });
    }
}
