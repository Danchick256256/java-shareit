package ru.practicum.shareit.ItemTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemControllerTests {
    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;
    @MockBean
    ItemService itemService;

    @SneakyThrows
    @Test
    void getAllItemsTests() {
        long userId = 1L;
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemService).getAllItemsByOwnerId(userId);
    }

    @SneakyThrows
    @Test
    void getItemByIdTests() {
        long userId = 1L;
        mockMvc.perform(get("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemService).getItemById(1L, userId);

    }

    @SneakyThrows
    @Test
    void createItemTest() {
        long userId = 1L;
        ItemDto itemDto = ItemDto.builder()
                .name("item")
                .description("description")
                .available(false)
                .build();
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemService).createItem(userId, itemDto);
    }

    @SneakyThrows
    @Test
    void updateItemTest() {
        long userId = 1L;
        long itemId = 1L;

        ItemDto itemDto = ItemDto.builder()
                .available(true)
                .build();

        mockMvc.perform(patch("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemService).updateItem(userId, itemId, itemDto);
    }

    @SneakyThrows
    @Test
    void searchItemsTest() {
        long userId = 1L;
        String text = "text";
        mockMvc.perform(get("/items/search?text={text}", text)
                        .header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemService).searchItems(text);
    }

    @SneakyThrows
    @Test
    void addCommentTest() {
        long userId = 1L;

        CommentDto commentDto = CommentDto.builder()
                .text("text")
                .build();

        mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemService).createComment(commentDto, userId, 1L);
    }
}
