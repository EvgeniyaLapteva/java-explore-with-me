package ru.practicum.ewm.controller.publicController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.comments.CommentDto;
import ru.practicum.ewm.service.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/comments")
public class PublicCommentsController {

    private final CommentService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getAllCommentsOfEventByPublic(@RequestParam Long eventId,
                                                          @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                          @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Публичный запрос на получение списка всех комментариев к событию id = {}", eventId);
        return service.getAllCommentsOfEventByPublic(eventId, from, size);
    }

    @GetMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto getCommentBuIdPublic(@PathVariable Long commentId) {
        log.info("Публичный запрос на получение комментария по id = {}", commentId);
        return service.getCommentByIdPublic(commentId);
    }
}
