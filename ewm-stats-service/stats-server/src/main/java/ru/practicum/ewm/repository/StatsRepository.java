package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.StatsForGetDto;
import ru.practicum.ewm.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Hit, Long> {

    List<StatsForGetDto> findAllByTimestampAndUri(LocalDateTime start, LocalDateTime end, List<String> uris);
}
