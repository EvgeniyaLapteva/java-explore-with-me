package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.client.StatsClient;
import ru.practicum.ewm.dto.events.*;
import ru.practicum.ewm.exception.model.ConditionsAreNotMetException;
import ru.practicum.ewm.exception.model.ObjectNotFoundException;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model.enums.EventSort;
import ru.practicum.ewm.model.enums.EventState;
import ru.practicum.ewm.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EventServiceImpl implements EventService {

    private final EventRepository repository;

    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    private final LocationRepository locationRepository;

    private final ParticipationRequestRepository requestRepository;

    private final StatsClient client;

    private static final LocalDateTime NOW = LocalDateTime.now();
    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> getEventsByUserId(Long userId, Integer from, Integer size) {
        PageRequest page = PageRequest.of(from / size, size);
        List<Event> events = repository.findAllByInitiatorId(userId, page);
        log.info("Получен список событий, созданных пользователем id = {}", userId);
        return events.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto createEvent(Long userId, NewEventDto eventDto) {
        if (!eventDto.getEventDate().isAfter(NOW.plusHours(2L))) {
            throw new ConditionsAreNotMetException("Дата события должна быть не раньше, чем через два часа после" +
                    "объявления о событии");
        }
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь" +
                        "с id = " + userId + " не найден"));
        Event event = EventMapper.toEvent(eventDto);
        locationRepository.save(event.getLocation());
        event.setInitiator(initiator);
        Long categoryId = eventDto.getCategory();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ObjectNotFoundException("Category with id=" + categoryId + " was not found"));
        event.setCategory(category);
        event.setCreatedOn(NOW);
        Event savedEvent = repository.save(event);
        log.info("Добавлено событие: {}", savedEvent);
        return EventMapper.toEventFullDto(savedEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEventByIdByUser(Long userId, Long eventId) {
        return null;
    }

    @Override
    public EventFullDto updateEventByIdByUser(Long userId, Long eventId, UpdateEventUserRequest eventDto) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> getAllEventsByAdmin(List<Long> users, List<EventState> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        return null;
    }

    @Override
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest eventDto) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getEventsPublic(String text, List<Long> categories, Boolean paid,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                               Boolean onlyAvailable, EventSort sort, Integer from,
                                               Integer size, String uri, String ip) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEventByIdPublic(Long id, String uri, String ip) {
        return null;
    }
}
