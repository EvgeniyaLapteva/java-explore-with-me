package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.HitForPostDto;
import ru.practicum.ewm.StatsForGetDto;
import ru.practicum.ewm.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsController {

    private final StatsService service;

    private final String forFormatter = "yyyy-MM-dd HH:mm:ss";

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void createHit(@Valid @RequestBody HitForPostDto hitDto) {
        log.info("Запрос на сохранение статистики");
        service.createHit(hitDto);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<StatsForGetDto> getStats(@RequestParam @DateTimeFormat(pattern = forFormatter) LocalDateTime start,
                                         @RequestParam @DateTimeFormat(pattern = forFormatter) LocalDateTime end,
                                         @RequestParam(required = false) List<String> uris,
                                         @RequestParam(defaultValue = "false") boolean unique) {
        log.info("Запрос на получение статистики");
        return service.getStats(start, end, uris, unique);
    }
}
