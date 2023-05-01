package ru.practicum.shareit.UserTests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.practicum.shareit.user.DTO.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.util.UserMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
class UserMapperTests {
    @Test
    void dtoToUserTest() {
        UserDto userDto = UserDto.builder()
                .name("name")
                .email("email@gmail.com")
                .build();

        User user = UserMapper.toUser(userDto);

        Assertions.assertThat(user)
                .hasFieldOrPropertyWithValue("name", "name")
                .hasFieldOrPropertyWithValue("email", "email@gmail.com");
    }
}
