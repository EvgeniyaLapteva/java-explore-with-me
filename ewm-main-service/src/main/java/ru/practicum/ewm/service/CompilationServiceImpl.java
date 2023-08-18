package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.compilations.CompilationDto;
import ru.practicum.ewm.dto.compilations.NewCompilationDto;
import ru.practicum.ewm.exception.model.ObjectNotFoundException;
import ru.practicum.ewm.mapper.CompilationMapper;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.repository.CompilationRepository;
import ru.practicum.ewm.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository repository;

    private final EventRepository eventRepository;

    @Override
    public CompilationDto createCompilation(NewCompilationDto compilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(compilationDto);
        if (!compilationDto.getEvents().isEmpty()) {
            compilation.setEvents(eventRepository.findByIdIn(compilationDto.getEvents()));
        }
        Compilation savedCompilation = repository.save(compilation);
        log.info("Создана новая подборка событий id = {}", savedCompilation.getId());
        return CompilationMapper.toCompilationDto(savedCompilation);
    }

    @Override
    public void deleteCompilation(Long compId) {
        getCompilation(compId);
        repository.deleteById(compId);
        log.info("Подборка событий id = {} удалена", compId);
    }

    @Override
    public CompilationDto updateCompilation(Long compId, NewCompilationDto compilationDto) {
        Compilation compilation = getCompilation(compId);
        List<Event> events;
        if (compilationDto.getPinned() != null) {
            compilation.setPinned(compilation.getPinned());
        }
        if (compilationDto.getTitle() != null) {
            compilation.setTitle(compilationDto.getTitle());
        }
        if (compilationDto.getEvents() != null) {
            events = eventRepository.findByIdIn(compilationDto.getEvents());
            compilation.setEvents(events);
        }
        Compilation updatedCompilation = repository.save(compilation);
        log.info("Compilation id = {} updated", compId);
        return CompilationMapper.toCompilationDto(updatedCompilation);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        PageRequest page = PageRequest.of(from / size, size);
        List<Compilation> compilations;
        if (pinned) {
            compilations = repository.findByPinned(true, page);
        } else {
            compilations = repository.findAll(page).getContent();
        }
        log.info("Получили список подборок событий");
        return compilations.stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = getCompilation(compId);
        log.info("Получили подборку событий id = {}", compId);
        return CompilationMapper.toCompilationDto(compilation);
    }

    private Compilation getCompilation(Long compId) {
        return repository.findById(compId).orElseThrow(
                () -> new ObjectNotFoundException("Compilation with id = " + compId + "was not found"));
    }
}
