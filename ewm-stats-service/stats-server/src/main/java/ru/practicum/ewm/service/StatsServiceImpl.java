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
        repository.save(hitForSave);
        log.info("Информация сохранена");
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatsForGetDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (unique) {
            if (uris != null) {
                log.info("Получили статистику по заданным uri и уникальным ip");
                return repository.getAllByTimestampAndUriUnique(start,end, uris);
            } else {
                log.info("Получили статистику по уникальным ip");
                return repository.getAllByTimestampUnique(start, end);
            }
        } else {
            if (uris != null) {
                log.info("Получили общую статистику по заданным uri");
                return repository.getAllByTimestampAndUri(start, end, uris);
            } else {
                log.info("Получили общую статистику");
                return repository.getAllByTimestamp(start, end);
            }
        }
    }
}
