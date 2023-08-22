package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.compilations.CompilationDto;
import ru.practicum.ewm.dto.compilations.NewCompilationDto;
import ru.practicum.ewm.dto.events.EventShortDto;
import ru.practicum.ewm.exception.model.ObjectNotFoundException;
import ru.practicum.ewm.mapper.CompilationMapper;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.enums.RequestStatus;
import ru.practicum.ewm.repository.CompilationRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.ParticipationRequestRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository repository;

    private final EventRepository eventRepository;

    private final ParticipationRequestRepository requestRepository;

    @Transactional
    @Override
    public CompilationDto createCompilation(NewCompilationDto compilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(compilationDto);
        Set<Long> eventsId = compilationDto.getEvents();
        if (eventsId != null) {
            Set<Event> events = eventRepository.findAllByIdIn(eventsId);
            compilation.setEvents(events);
        }
        Compilation savedCompilation = repository.save(compilation);
        CompilationDto result = getCompilationDtoFull(savedCompilation);
        log.info("Создана новая подборка событий id = {}", savedCompilation.getId());
        return result;
    }

    @Transactional
    @Override
    public void deleteCompilation(Long compId) {
        getCompilation(compId);
        repository.deleteById(compId);
        log.info("Подборка событий id = {} удалена", compId);
    }

    @Transactional
    @Override
    public CompilationDto updateCompilation(Long compId, NewCompilationDto compilationDto) {
        Compilation compilation = getCompilation(compId);
        if (compilationDto.getTitle() != null && !compilationDto.getTitle().isBlank()) {
            compilation.setTitle(compilationDto.getTitle());
        }
        if (compilationDto.getPinned() != null) {
            compilation.setPinned(compilationDto.getPinned());
        }
        if (compilationDto.getEvents() != null) {
            Set<Long> eventsId = compilationDto.getEvents();
            Set<Event> events = eventRepository.findAllByIdIn(eventsId);
            compilation.setEvents(events);
        }
        log.info("Compilation compId = {} updated", compId);
        return getCompilationDtoFull(compilation);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        PageRequest page = PageRequest.of(from / size, size);
        List<Compilation> compilations;
        if (pinned != null) {
            compilations = repository.findByPinned(pinned, page);
        } else {
            compilations = repository.findAll(page).getContent();
        }
        List<CompilationDto> compilationsDto = compilations
                .stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
        for (CompilationDto dto : compilationsDto) {
            List<EventShortDto> compilationEvents = dto.getEvents();
            if (compilationEvents != null) {
                for (EventShortDto event : compilationEvents) {
                    Integer confirmedRequests = requestRepository.countAllByEventIdAndStatus(event.getId(),
                            RequestStatus.CONFIRMED);
                    event.setConfirmedRequests(confirmedRequests);
                }
            }
        }
        log.info("Получили список подборок событий");
        return compilationsDto;
    }

    @Transactional(readOnly = true)
    @Override
    public CompilationDto getCompilationById(Long id) {
        Compilation compilation = getCompilation(id);
        CompilationDto result = getCompilationDtoFull(compilation);
        log.info("Получили подборку событий id = {}", id);
        return result;
    }

    private Compilation getCompilation(Long compId) {
        return repository.findById(compId).orElseThrow(
                () -> new ObjectNotFoundException("Compilation with id = " + compId + "was not found"));
    }

    private CompilationDto getCompilationDtoFull(Compilation compilation) {
        CompilationDto dto = CompilationMapper.toCompilationDto(compilation);
        List<EventShortDto> compilationEvents = dto.getEvents();
        if (compilationEvents != null) {
            for (EventShortDto event : compilationEvents) {
                Integer confirmedRequests = requestRepository.countAllByEventIdAndStatus(event.getId(),
                        RequestStatus.CONFIRMED);
                event.setConfirmedRequests(confirmedRequests);
            }
        }
        return dto;
    }
}
