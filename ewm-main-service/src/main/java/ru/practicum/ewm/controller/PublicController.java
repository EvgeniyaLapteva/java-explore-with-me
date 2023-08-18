package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.compilations.CompilationDto;
import ru.practicum.ewm.dto.events.EventFullDto;
import ru.practicum.ewm.dto.events.EventShortDto;
import ru.practicum.ewm.service.CategoryService;
import ru.practicum.ewm.service.CompilationService;
import ru.practicum.ewm.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PublicController {

    private final CategoryService categoryService;

    private final EventService eventService;

    private final CompilationService compilationService;

    private static final String FOR_FORMATTER = "yyyy-MM-dd HH:mm:ss";

    @GetMapping("/compilations")
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationDto> getCompilations(@RequestParam(defaultValue = "false") Boolean pinned,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Запрос на получение списка подборок событий");
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto getCompilationById(@PathVariable Long compId) {
        log.info("Запрос на получения подборки событий по id = {}", compId);
        return compilationService.getCompilationById(compId);
    }

    @GetMapping("/categories")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                           @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Запрос на получение списка категорий");
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getCategoryById(@PathVariable Long catId) {
        log.info("Запрос на получение категории по id = {}", catId);
        return categoryService.getCategoryById(catId);
    }

    @GetMapping("/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEventsPublic(@RequestParam(required = false) String text,
                                         @RequestParam(required = false) List<Long> categories,
                                         @RequestParam(required = false) Boolean paid,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = FOR_FORMATTER)
                                             LocalDateTime rangeStart,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = FOR_FORMATTER)
                                             LocalDateTime rangeEnd,
                                         @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
                                         @RequestParam(required = false) String sort,
                                         @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                         @RequestParam(defaultValue = "10") @Positive Integer size,
                                               HttpServletRequest request) {
        log.info("Публичный запрос на получение списка событий");
        return eventService.getEventsPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from,
                size, request);
    }

    @GetMapping("events/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventByIdPublic(@Positive @PathVariable Long id, HttpServletRequest request) {
        log.info("Публичный запрос на получение события по id = {}", id);
        return eventService.getEventByIdPublic(id, request);
    }
}
