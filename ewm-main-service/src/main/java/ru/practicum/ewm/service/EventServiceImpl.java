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
import ru.practicum.ewm.mapper.LocationMapper;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.Location;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model.enums.EventSort;
import ru.practicum.ewm.model.enums.EventState;
import ru.practicum.ewm.model.enums.StateAction;
import ru.practicum.ewm.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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
        validateEventDate(eventDto.getEventDate());
        User initiator = getUser(userId);
        Long catId = eventDto.getCategory();
        Category category = getCategory(catId);
        Location location = LocationMapper.toLocation(eventDto.getLocation());
        locationRepository.save(location);
        Event event = EventMapper.toEvent(eventDto, category, initiator, location);
        Event savedEvent = repository.save(event);
        log.info("Добавлено событие: {}", savedEvent);
        return EventMapper.toEventFullDto(savedEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEventByIdByUser(Long userId, Long eventId) {
        Event event = getEvent(eventId);
        validateInitiator(userId, event.getInitiator().getId());
        log.info("Получили событие id = {} пользователя id = {}", eventId, userId);
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto updateEventByIdByUser(Long userId, Long eventId, UpdateEventDto eventDto) {
        validateEventDate(eventDto.getEventDate());
        getUser(userId);
        Event event = getEvent(eventId);
        validateInitiator(userId, event.getInitiator().getId());
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ConditionsAreNotMetException("Пользователь id = " + userId + "не может редактировать " +
                    "опубликованное событие id = " + eventId);
        }
        Event updatedEvent = updateEvent(event, eventDto);
        log.info("Обновили событие id = {}, добавленное текущим пользователем id = {}", eventId, userId);
        return EventMapper.toEventFullDto(updatedEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> getAllEventsByAdmin(List<Long> users, List<EventState> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        return null;
    }

    @Override
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventDto eventDto) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getEventsPublic(String text, List<Long> categories, Boolean paid,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                               Boolean onlyAvailable, EventSort sort, Integer from,
                                               Integer size, String uri, String ip) {
        return null;
        //добавляем в статистику
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEventByIdPublic(Long id, String uri, String ip) {
        return null;
        //добавляем в статистику
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь" +
                        "с id = " + userId + " не найден"));
    }

    private Event getEvent(Long eventId) {
        return repository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Event with id=" + eventId + " was not found"));
    }

    private Category getCategory(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new ObjectNotFoundException("Category with id=" + catId + " was not found"));
    }

    private void validateInitiator(Long userId, Long initiatorId) {
        if (!Objects.equals(userId, initiatorId)) {
            throw new ConditionsAreNotMetException("Initiator of event has id = " + initiatorId +
                    " but user id is " + userId);
        }
    }

    private void validateEventDate(LocalDateTime eventDate) {
        if (!eventDate.isAfter(NOW.plusHours(2L))) {
            throw new ConditionsAreNotMetException("Дата события должна быть не раньше, чем через два часа после" +
                    "объявления о событии");
        }
    }

    private Event updateEvent(Event event, UpdateEventDto updateEventDto) {
        if (!updateEventDto.getAnnotation().isBlank()) {
            event.setAnnotation(updateEventDto.getAnnotation());
        }
        Long catId = updateEventDto.getCategory();
        if (catId != null) {
            Category category = getCategory(catId);
            event.setCategory(category);
        }
        if (!updateEventDto.getDescription().isBlank()) {
            event.setDescription(updateEventDto.getDescription());
        }
        if (updateEventDto.getEventDate() != null) {
            event.setEventDate(updateEventDto.getEventDate());
        }
        if (updateEventDto.getLocation() != null) {
            Location location = LocationMapper.toLocation(updateEventDto.getLocation());
            locationRepository.save(location);
            event.setLocation(location);
        }
        if (updateEventDto.getPaid() != null) {
            event.setPaid(updateEventDto.getPaid());
        }
        if (updateEventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventDto.getParticipantLimit());
        }
        if (updateEventDto.getRequestModeration() != null) {
            event.setRequestModeration(updateEventDto.getRequestModeration());
        }
        if (!updateEventDto.getTitle().isBlank()) {
            event.setTitle(updateEventDto.getTitle());
        }
        if (updateEventDto.getStateAction() != null) {
            if (updateEventDto.getStateAction() == StateAction.PUBLISH_EVENT) {
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(NOW);
            } else if (updateEventDto.getStateAction() == StateAction.REJECT_EVENT ||
            updateEventDto.getStateAction() == StateAction.CANCEL_REVIEW) {
                event.setState(EventState.CANCELED);
            } else if (updateEventDto.getStateAction() == StateAction.SEND_TO_REVIEW) {
                event.setState(EventState.PENDING);
            }
        }
        return repository.save(event);
    }
}
