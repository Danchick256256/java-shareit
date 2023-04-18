package ru.practicum.shareit.RequestsTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.requests.controller.RequestsController;
import ru.practicum.shareit.requests.dto.RequestsDto;
import ru.practicum.shareit.requests.dto.RequestsResponse;
import ru.practicum.shareit.requests.service.RequestsService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RequestsController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RequestsControllerTests {
    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;
    @MockBean
    RequestsService requestService;

    @SneakyThrows
    @Test
    void getRequestsById() {
        long userId = 1L;
        long requestId = 1L;
        mockMvc.perform(get("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(requestService).getRequestById(requestId, userId);
    }

    @SneakyThrows
    @Test
    void getAllRequestsTests() {
        long userId = 1L;
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(requestService).getAllRequests(userId);
    }

    @SneakyThrows
    @Test
    void getAllRequestsFromOwner() {
        long userId = 1L;
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(requestService).getRequestByOwnerId(userId);
    }

    @SneakyThrows
    @Test
    void createRequestTest() {
        long userId = 1L;
        RequestsDto incomeDto = RequestsDto.builder()
                .description("text")
                .build();
        RequestsResponse requestsResponse = RequestsResponse.builder()
                .id(1L)
                .owner(1L)
                .creationDate(LocalDateTime.now())
                .items(Collections.emptyList())
                .description("text")
                .build();

        when(requestService.createRequest(any(), anyLong())).thenReturn(requestsResponse);
        String content = mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incomeDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertEquals(objectMapper.writeValueAsString(requestsResponse), content);
    }
}
