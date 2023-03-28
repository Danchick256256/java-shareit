package ru.practicum.shareit.item.util;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class CommentMapper {
    public static Comment dtoToComment(CommentDto dto, Item item, User author) {
        return Comment.builder()
                .item(item)
                .author(author)
                .text(dto.getText())
                .created(dto.getCreated())
                .build();
    }
}
