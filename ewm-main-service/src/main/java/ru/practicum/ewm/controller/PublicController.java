package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.compilations.CompilationDto;
import ru.practicum.ewm.dto.events.EventFullDto;
import ru.practicum.ewm.dto.events.EventShortDto;
import ru.practicum.ewm.model.enums.EventSort;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PublicController {

    private static final String FOR_FORMATTER = "yyyy-MM-dd HH:mm:ss";

    @GetMapping("/compilations")
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Запрос на получение списка подборок событий");
        return new ArrayList<>();
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilationById(@PathVariable long compId) {
        log.info("Запрос на получения подборки событий по id = {}", compId);
        return null;
    }

    @GetMapping("/categories")
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                           @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Запрос на получение списка категорий");
        return new ArrayList<>();
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoryById(@PathVariable long catId) {
        log.info("Запрос на получение категории по id = {}", catId);
        return null;
    }

    @GetMapping("/events")
    public List<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                         @RequestParam(required = false) List<Long> categories,
                                         @RequestParam(required = false) boolean paid,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = FOR_FORMATTER)
                                             LocalDateTime rangeStart,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = FOR_FORMATTER)
                                             LocalDateTime rangeEnd,
                                         @RequestParam(required = false) boolean onlyAvailable,
                                         @RequestParam(required = false) EventSort sort,
                                         @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                         @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Запрос на получение списка событий");
//        это публичный эндпоинт, соответственно в выдаче должны быть только опубликованные события
//        текстовый поиск (по аннотации и подробному описанию) должен быть без учета регистра букв
//        если в запросе не указан диапазон дат [rangeStart-rangeEnd], то нужно выгружать события, которые произойдут позже текущей даты и времени
//        информация о каждом событии должна включать в себя количество просмотров и количество уже одобренных заявок на участие
//        информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики
        return new ArrayList<>();
    }

    @GetMapping("events/{id}")
    public EventFullDto getEventById(@PathVariable long id) {

        log.info("Запрос на получение события по id = {}", id);
//        событие должно быть опубликовано
//        информация о событии должна включать в себя количество просмотров и количество подтвержденных запросов
//        информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики
//        В случае, если события с заданным id не найдено, возвращает статус код 404
        return null;
    }
}
