package ru.practicum.shareit.UserTests;

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
import ru.practicum.shareit.user.DTO.UserDto;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.exception.UserNotUniqueEmailException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ControllerTests {
    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;
    @MockBean
    private final UserService userService;

    @SneakyThrows
    @Test
    void getByIdTest() {
        long userId = 1L;
        mockMvc.perform(get("/users/{userId}", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService).getUserById(userId);
    }

    @SneakyThrows
    @Test
    void getAllTest() {
        long userId = 1L;
        mockMvc.perform(get("/users", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService).getAllUsers();
    }

    @SneakyThrows
    @Test
    void createUserTest() {
        UserDto userDto = UserDto.builder()
                .name("user")
                .email("12user@gmail.com")
                .build();
        User user = User.builder()
                .id(1L)
                .name("user")
                .email("12user@gmail.com")
                .build();

        when(userService.createUser(any())).thenReturn(user);
        String result = mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertEquals(objectMapper.writeValueAsString(user), result);
    }

    @SneakyThrows
    @Test
    void createUserWithIncorrectEmailTest() {
        UserDto userIncomeDto = UserDto.builder()
                .name("User")
                .email("bademail.com")
                .build();

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userIncomeDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(userService, never()).createUser(any());
    }

    @SneakyThrows
    @Test
    void updateUserTest() {
        UserDto userInDto = UserDto.builder()
                .name("user")
                .email("23user@gmail.com")
                .build();
        User user = User.builder()
                .id(1L)
                .name("userUpdated")
                .email("23user@gmail.com")
                .build();

        when(userService.updateUser(anyLong(), any())).thenReturn(user);
        String result = mockMvc.perform(
                        patch("/users/{userId}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userInDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertEquals(objectMapper.writeValueAsString(user), result);
    }

    @SneakyThrows
    @Test
    void deleteUserTest() {
        long userId = 1L;
        mockMvc.perform(delete("/users/{userId}", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService).deleteUser(userId);
    }

    @SneakyThrows
    @Test
    void createUserWithExistingEmailTest() {
        UserDto userInDto = UserDto.builder()
                .name("user")
                .email("24user@gmail.com")
                .build();

        when(userService.createUser(any())).thenThrow(UserNotUniqueEmailException.class);
        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userInDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
}
