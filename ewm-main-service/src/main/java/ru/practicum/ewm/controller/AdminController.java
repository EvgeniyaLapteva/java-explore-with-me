package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.*;
import ru.practicum.ewm.dto.compilations.CompilationDto;
import ru.practicum.ewm.dto.compilations.NewCompilationDto;
import ru.practicum.ewm.dto.events.EventFullDto;
import ru.practicum.ewm.dto.events.UpdateEventAdminRequest;
import ru.practicum.ewm.dto.users.UserDto;
import ru.practicum.ewm.service.UserService;
import ru.practicum.ewm.validation.Create;
import ru.practicum.ewm.validation.Update;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin")
@Validated
public class AdminController {

    private final UserService userService;

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
                                           @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                           @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Запрос на получение списка событий");
        return new ArrayList<>();
//        Эндпоинт возвращает полную информацию обо всех событиях подходящих под переданные условия
//
//        В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long eventId,
                                    @Valid @RequestBody UpdateEventAdminRequest eventDto) {
        log.info("Запрос на обновление события по id = {}", eventId);
        return null;
    }

    @GetMapping("/users")
    public List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                  @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                  @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Запрос на получение списка пользователей");
        return userService.getUsers(ids, from, size);
    }

    @PostMapping("/users")
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Запрос на создание пользователя");
        return userService.createUser(userDto);
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUserById(@PathVariable Long userId) {
        log.info("Запрос на удаление пользователя id = {}", userId);
        userService.deleteUserById(userId);
    }

    @PostMapping("/compilations")
    public CompilationDto createCompilation(@Validated(Create.class) @RequestBody NewCompilationDto compilationDto) {
        log.info("Запрос на создание подборки событий");
        return null;
    }

    @DeleteMapping("/compilations/{compId}")
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("Запрос на удаление подборки событий id = {}", compId);
    }

    @PatchMapping("/compilations/{compId}")
    public CompilationDto updateCompilation(@PathVariable Long compId,
                                            @Validated(Update.class) @RequestBody NewCompilationDto compilationDto) {
        log.info("Запрос на обновление подборки событий id= {}", compId);
        return null;
    }
}
