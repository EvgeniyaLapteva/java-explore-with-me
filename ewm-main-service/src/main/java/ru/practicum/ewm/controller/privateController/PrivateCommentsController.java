package ru.practicum.ewm.controller.privateController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.comments.CommentDto;
import ru.practicum.ewm.dto.comments.NewCommentDto;
import ru.practicum.ewm.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users/{userId}/comments")
@Validated
public class PrivateCommentsController {

    private final CommentService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@PathVariable Long userId, @RequestParam Long eventId,
                                    @Valid @RequestBody NewCommentDto newCommentDto) {
        log.info("Запрос на создание комментария от пользователя id = {} на событие id = {}", userId, eventId);
        return service.createComment(userId, eventId, newCommentDto);
    }

    @PatchMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateCommentById(@PathVariable Long userId, @PathVariable Long commentId,
                                        @Valid @RequestBody NewCommentDto newCommentDto) {
        log.info("Запрос на обновление комментария id = {} от пользователя id = {}", commentId, userId);
        return service.updateCommentById(userId, commentId, newCommentDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getCommentsByAuthor(@PathVariable Long userId,
                                                @RequestParam(required = false) Long eventId,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Запрос на получение комментариев пользователя id = {}", userId);
        return service.getCommentsByAuthor(userId, eventId, from, size);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByIdByAuthor(@PathVariable Long userId, @PathVariable Long commentId) {
        log.info("Запрос на удаление комментария id = {} автором id = {}", commentId, userId);
        service.deleteCommentByIdByAuthor(userId, commentId);
    }
}
