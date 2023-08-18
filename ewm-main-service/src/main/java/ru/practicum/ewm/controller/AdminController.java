package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.*;
import ru.practicum.ewm.dto.compilations.CompilationDto;
import ru.practicum.ewm.dto.compilations.NewCompilationDto;
import ru.practicum.ewm.dto.events.EventFullDto;
import ru.practicum.ewm.dto.events.UpdateEventDto;
import ru.practicum.ewm.dto.users.UserDto;
import ru.practicum.ewm.model.enums.EventState;
import ru.practicum.ewm.service.CategoryService;
import ru.practicum.ewm.service.CompilationService;
import ru.practicum.ewm.service.EventService;
import ru.practicum.ewm.service.UserService;
import ru.practicum.ewm.validation.Create;
import ru.practicum.ewm.validation.Update;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin")
@Validated
public class AdminController {

    private final UserService userService;

    private final CategoryService categoryService;

    private final EventService eventService;

    private final CompilationService compilationService;

    private static final String FOR_FORMATTER = "yyyy-MM-dd HH:mm:ss";

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Запрос на добавление новой категории");
        return categoryService.createCategory(categoryDto);
    }

    @DeleteMapping("categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoryById(@PathVariable Long catId) {
        log.info("Запрос на удаление категории");
        categoryService.deleteCategoryById(catId);
    }

    @PatchMapping("categories/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto updateCategory(@PathVariable Long catId, @Valid @RequestBody CategoryDto categoryDto) {
        log.info("Запрос на изменение категории id = {}", catId);
        return  categoryService.updateCategory(catId, categoryDto);
    }

    @GetMapping("/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getAllEventsByAdmin(@RequestParam(required = false) List<Long> users,
                                                  @RequestParam(required = false) List<EventState> states,
                                                  @RequestParam(required = false) List<Long> categories,
                                                  @RequestParam(required = false) @DateTimeFormat(pattern = FOR_FORMATTER)
                                           LocalDateTime rangeStart,
                                                  @RequestParam(required = false) @DateTimeFormat(pattern = FOR_FORMATTER)
                                           LocalDateTime rangeEnd,
                                                  @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                  @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Запрос от администратора на получение списка событий");
        return eventService.getAllEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEventByAdmin(@PathVariable Long eventId,
                                           @Valid @RequestBody UpdateEventDto eventDto) {
        log.info("Запрос от администратора на обновление события по id = {}", eventId);
        return eventService.updateEventByAdmin(eventId, eventDto);
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                  @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                  @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Запрос на получение списка пользователей");
        return userService.getUsers(ids, from, size);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Запрос на создание пользователя");
        return userService.createUser(userDto);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable Long userId) {
        log.info("Запрос на удаление пользователя id = {}", userId);
        userService.deleteUserById(userId);
    }

    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@Validated(Create.class) @RequestBody NewCompilationDto compilationDto) {
        log.info("Запрос на создание подборки событий");
        return compilationService.createCompilation(compilationDto);
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("Запрос на удаление подборки событий id = {}", compId);
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto updateCompilation(@PathVariable Long compId,
                                            @Validated(Update.class) @RequestBody NewCompilationDto compilationDto) {
        log.info("Запрос на обновление подборки событий id= {}", compId);
        return compilationService.updateCompilation(compId, compilationDto);
    }
}
