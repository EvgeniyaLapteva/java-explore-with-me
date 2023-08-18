package ru.practicum.ewm.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.HitForPostDto;
import ru.practicum.ewm.StatsForGetDto;
import ru.practicum.ewm.client.StatsClient;
import ru.practicum.ewm.dto.events.*;
import ru.practicum.ewm.dto.participationRequest.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.participationRequest.EventRequestStatusUpdateResult;
import ru.practicum.ewm.dto.participationRequest.ParticipationRequestDto;
import ru.practicum.ewm.exception.model.ConditionsAreNotMetException;
import ru.practicum.ewm.exception.model.IncorrectRequestException;
import ru.practicum.ewm.exception.model.ObjectNotFoundException;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.mapper.LocationMapper;
import ru.practicum.ewm.mapper.ParticipationRequestMapper;
import ru.practicum.ewm.model.*;
import ru.practicum.ewm.model.enums.EventState;
import ru.practicum.ewm.model.enums.RequestStatus;
import ru.practicum.ewm.model.enums.StateAction;
import ru.practicum.ewm.repository.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
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

    private final ObjectMapper mapper = new ObjectMapper();

    private static final LocalDateTime START = LocalDateTime.of(2000, 1, 1, 0, 0);

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
    public List<EventFullDto> getAllEventsByAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                                                  LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from,
                                                  Integer size) {
        PageRequest page = PageRequest.of(from / size, size);
        List<Event> events = repository.getAllEventsByAdmin(users, states, categories, rangeStart, rangeEnd, page);
        List<Long> eventIds = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());
        Map<Long, Long> views = getViews(eventIds);
        for (Event event : events) {
            event.setViews(views.getOrDefault(event.getId(), 0L));
            repository.save(event);
        }
        log.info("Получили полную информацию обо всех событиях подходящих под переданные условия в ответ на запрос от " +
                "администратора");
        return events.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventDto eventDto) {
        Event event = getEvent(eventId);
        if (eventDto.getStateAction() != null) {
            if (eventDto.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
                if (!event.getState().equals(EventState.PENDING)) {
                    throw new ConditionsAreNotMetException("Нельзя опубликовать событие id = " + eventId + "поскольку " +
                            "оно уже опубликовано");
                }
                event.setPublishedOn(NOW);
                event.setState(EventState.PUBLISHED);
            } else {
                if (!event.getState().equals(EventState.PENDING)) {
                    throw new ConditionsAreNotMetException("Событие id = " + eventId + "нельзя отклонить тк оно не в " +
                            "статусе PENDING");
                }
                event.setState(EventState.CANCELED);
            }
        }
        Event updatedEvent = updateEvent(event, eventDto);
        return EventMapper.toEventFullDto(updatedEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getEventsPublic(String text, List<Long> categories, Boolean paid,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                               Boolean onlyAvailable, String sort, Integer from,
                                               Integer size, HttpServletRequest request) {
        if (rangeStart!= null && rangeEnd != null) {
            if (rangeStart.isAfter(rangeEnd)) {
                throw new IncorrectRequestException("RangeStart is after rangeEnd");
            }
        }
        PageRequest page = PageRequest.of(from / size, size);
        List<Event> events = repository.getEventsPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                sort, page);
        List<Long> eventIds = events.stream().map(Event::getId).collect(Collectors.toList());
        sendStats(request);
        Map<Long, Long> views = getViews(eventIds);
        for (Event event : events) {
            event.setViews(views.get(event.getId()));
            repository.save(event);
        }
        log.info("олучили список событий по заданным параметрам в ответ на публичный запрос");
        return events.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEventByIdPublic(Long id, HttpServletRequest request) {
        Event event = getEvent(id);
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ObjectNotFoundException("Событие id = " + id + " еще не опубликовано");
        }
        sendStats(request);
        Map<Long, Long> views = getViews(List.of(id));
        event.setViews(views.getOrDefault(id, 0L));
        repository.save(event);
        log.info("Получили событие по id = {} в ответ на публичный запрос", id);
        return EventMapper.toEventFullDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getRequestsByUserIdAndEventId(Long userId, Long eventId) {
        Event event = getEvent(eventId);
        validateInitiator(userId, event.getInitiator().getId());
        List<ParticipationRequest> requests = requestRepository.findByEventId(eventId);
        log.info("Получен список заявок на участие в событии id = {}", eventId);
        return requests.stream()
                .map(ParticipationRequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResult updateRequestsStatusByUserId(Long userId, Long eventId,
                                                                       EventRequestStatusUpdateRequest request) {
        User user = getUser(userId);
        Event event = getEvent(eventId);
        EventRequestStatusUpdateResult result = EventRequestStatusUpdateResult.builder()
                .confirmedRequests(Collections.emptyList())
                .rejectedRequests(Collections.emptyList())
                .build();
        if (!user.getId().equals(event.getInitiator().getId())) {
            throw new ConditionsAreNotMetException("The user id = " + userId + " is not the initiator of event id = " +
                    eventId);
        }
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            return  result;
        }
        if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ConditionsAreNotMetException("Exceeded the limit of participants");
        }
        List<ParticipationRequest> confirmedRequests = new ArrayList<>();
        List<ParticipationRequest> rejectedRequests = new ArrayList<>();
        long freePlace = event.getParticipantLimit() - event.getConfirmedRequests();
        List<ParticipationRequest> requests = requestRepository.findAllById(request.getRequestIds());
        for (ParticipationRequest participationRequest : requests) {
            if (!participationRequest.getStatus().equals(RequestStatus.PENDING)) {
                throw new ConditionsAreNotMetException("Requests status has to be PENDING");
            }
            if (request.getStatus().equals(RequestStatus.CONFIRMED) && freePlace > 0) {
                participationRequest.setStatus(RequestStatus.CONFIRMED);
                event.setConfirmedRequests(event.getConfirmedRequests() + 1L);
                confirmedRequests.add(participationRequest);
                freePlace--;
            } else {
                participationRequest.setStatus(RequestStatus.REJECTED);
                rejectedRequests.add(participationRequest);
            }
        }
        result.setConfirmedRequests(confirmedRequests.stream()
                .map(ParticipationRequestMapper::toRequestDto).collect(Collectors.toList()));
        result.setRejectedRequests(rejectedRequests.stream()
                .map(ParticipationRequestMapper::toRequestDto).collect(Collectors.toList()));
        repository.save(event);
        requestRepository.saveAll(requests);
        log.info("Обновили статусы заявок на участии в событии id = {} пользователя id = {}", eventId, userId);
        return result;
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

    private Map<Long, Long> getViews(List<Long> eventIds) {
        List<String> uris = new ArrayList<>();
        for (Long eventId : eventIds) {
            String uri = "/events/" + eventId;
            uris.add(uri);
        }
        ResponseEntity<Object> response = client.getStats(START, NOW, uris, true);
        log.info("Запрос на получение статистики по списку uris: {}", uris);
        List<StatsForGetDto> stats = mapper.convertValue(response.getBody(), new TypeReference<>() {});
        Map<Long, Long> views = new HashMap<>();
        for (StatsForGetDto dto : stats) {
            String[] split = dto.getUri().split("/");
            String id = split[2];
            views.put(Long.parseLong(id), dto.getHits());
        }
        return views;
    }

    private void sendStats(HttpServletRequest request) {
        HitForPostDto hit = HitForPostDto.builder()
                .app("ewm-main-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(NOW)
                .build();
        client.createHit(hit);
    }
}
