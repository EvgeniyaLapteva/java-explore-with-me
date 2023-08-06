package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.HitForPostDto;
import ru.practicum.ewm.StatsForGetDto;
import ru.practicum.ewm.mapper.HitMapper;
import ru.practicum.ewm.model.Hit;
import ru.practicum.ewm.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StatsServiceImpl implements StatsService {

    private final StatsRepository repository;

    @Override
    public void createHit(HitForPostDto hitDto) {
        Hit hitForSave = HitMapper.toHit(hitDto);
        Hit savedHit = repository.save(hitForSave);
        log.info("Информация сохранена");
    }

    @Override
    public List<StatsForGetDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        return null;
    }
}
