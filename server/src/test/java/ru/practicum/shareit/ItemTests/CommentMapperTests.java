package ru.practicum.shareit.ItemTests;

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
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.util.CommentMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
class CommentMapperTests {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Test
    @Sql(value = {"/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql" })
    void dtoToCommentTest() {
        CommentDto commentDto = CommentDto.builder()
                .text("text")
                .build();

        Item item = itemRepository.findById(1L).get();

        User user = userRepository.findById(1L).get();

        Comment comment = CommentMapper.dtoToComment(commentDto, item, user);

        Assertions.assertThat(comment)
                .hasFieldOrPropertyWithValue("item", item)
                .hasFieldOrPropertyWithValue("text", "text")
                .hasFieldOrProperty("created")
                .hasFieldOrPropertyWithValue("author", user);
    }

    @Test
    @Sql(value = {"/test-schema.sql", "/test-create-user.sql", "/test-create-item.sql" })
    void commentToDtoTest() {
        LocalDateTime now = LocalDateTime.now();

        Item item = itemRepository.findById(1L).get();

        User user = userRepository.findById(1L).get();

        Comment comment = Comment.builder()
                .id(1L)
                .created(now)
                .author(user)
                .item(item)
                .text("text")
                .build();

        CommentDto commentDto = CommentMapper.commentToDto(comment);

        Assertions.assertThat(commentDto)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("text", "text")
                .hasFieldOrPropertyWithValue("created", now)
                .hasFieldOrPropertyWithValue("authorName", user.getName());
    }
}
