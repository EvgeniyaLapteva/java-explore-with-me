package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin")
public class AdminController {

    private static final String FOR_FORMATTER = "yyyy-MM-dd HH:mm:ss";

    @PostMapping("/categories")
    public CategoryDto createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Запрос на добавление новой категории");
        return null;
    }

    @DeleteMapping("categories/{catId}")
    public void deleteCategoryById(@PathVariable Long catId) {
        log.info("Запрос на удаление категории");
    }

    @PatchMapping("categories/{catId}")
    public CategoryDto updateCategory(@PathVariable Long catId, @RequestBody CategoryDto categoryDto) {
        log.info("Запрос на изменение категории");
        return  null;
    }

    @GetMapping("/events")
    public List<EventFullDto> getAllEvents(@RequestParam(required = false) List<Long> users,
                                           @RequestParam(required = false) List<String> states,
                                           @RequestParam(required = false) List<Long> categories,
                                           @RequestParam(required = false) @DateTimeFormat(pattern = FOR_FORMATTER)
                                           LocalDateTime rangeStart,
                                           @RequestParam(required = false) @DateTimeFormat(pattern = FOR_FORMATTER)
                                           LocalDateTime rangeEnd,
                                           @RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size) {
        log.info("Запрос на получение списка событий");
        return new ArrayList<>();
//        Эндпоинт возвращает полную информацию обо всех событиях подходящих под переданные условия
//
//        В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long eventId, @RequestBody UpdateEventAdminRequest eventDto) {
        log.info("Запрос на получение события по id = {}", eventId);
        return null;
    }

    @GetMapping("/users")
    public List<UserDto> getUsers(@RequestParam(required = false) List<Long>ids,
                                  @RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size) {
        log.info("Запрос на получение списка пользователей");
        return new ArrayList<>();
    }

    @PostMapping("/users")
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Запрос на создание пользователя");
        return null;
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUSerById(@PathVariable Long userId) {
        log.info("Запрос на удаление пользователя id = {}", userId);
    }

    @PostMapping("/compilations")
    public CompilationDto createCompilation(@RequestBody CompilationDto compilationDto) {
        log.info("Запрос на создание подборки событий");
        return null;
    }

    @DeleteMapping("/compilations/{compId}")
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("Запрос на удаление подборки событий id = {}", compId);
    }

    @PatchMapping("/compilations/{compId}")
    public CompilationDto updateCompilation(@PathVariable Long compId, @RequestBody CompilationDto compilationDto) {
        log.info("Запрос на обновление подборки событий id= {}", compId);
        return null;
    }
}
