package ru.practicum.ewm.mapper;

import ru.practicum.ewm.dto.comments.CommentDto;
import ru.practicum.ewm.model.Comment;

public class CommentMapper {

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .author(UserMapper.toUserShortDto(comment.getAuthor()))
                .publishedOn(comment.getPublishedOn())
                .eventId(comment.getEvent().getId())
                .build();
    }
}
