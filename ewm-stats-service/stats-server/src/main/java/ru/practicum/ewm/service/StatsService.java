package ru.practicum.ewm.service;

import ru.practicum.ewm.HitForPostDto;
import ru.practicum.ewm.StatsForGetDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    void createHit(HitForPostDto hitDto);

    List<StatsForGetDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}