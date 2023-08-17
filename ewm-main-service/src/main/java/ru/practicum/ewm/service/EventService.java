package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.events.*;
import ru.practicum.ewm.model.enums.EventSort;
import ru.practicum.ewm.model.enums.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    List<EventFullDto> getEventsByUserId(Long userId, Integer from, Integer size);

    EventFullDto createEvent(Long userId, NewEventDto eventDto);

    EventFullDto getEventByIdByUser(Long userId, Long eventId);

    EventFullDto updateEventByIdByUser(Long userId, Long eventId, UpdateEventUserRequest eventDto);

    List<EventFullDto> getAllEventsByAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                                           LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest eventDto);

    List<EventShortDto> getEventsPublic(String text, List<Long> categories, Boolean paid,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                               Boolean onlyAvailable, EventSort sort, Integer from, Integer size,
                                        String uri, String ip);

    EventFullDto getEventByIdPublic(Long id, String uri, String ip);
}
