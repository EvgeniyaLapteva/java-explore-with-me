package ru.practicum.ewm.mapper;

import ru.practicum.ewm.dto.events.EventFullDto;
import ru.practicum.ewm.dto.events.EventShortDto;
import ru.practicum.ewm.dto.events.NewEventDto;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.enums.EventState;

public class EventMapper {

    public static Event toEvent(NewEventDto newEventDto) {
        Event event = new Event();
        event.setAnnotation(newEventDto.getAnnotation());
        event.setDescription(newEventDto.getDescription());
        event.setEventDate(newEventDto.getEventDate());
        event.setLocation(LocationMapper.toLocation(newEventDto.getLocation()));
        event.setState(EventState.PENDING);
        event.setTitle(newEventDto.getTitle());
        if (newEventDto.getPaid() != null) {
            event.setPaid(newEventDto.getPaid());
        } else {
            event.setPaid(false);
        }
        if (newEventDto.getParticipantLimit() == null) {
            event.setParticipantLimit(0);
        } else {
            event.setParticipantLimit(newEventDto.getParticipantLimit());
        }
        if (newEventDto.getRequestModeration() == null) {
            event.setRequestModeration(true);
        } else  {
            event.setRequestModeration(newEventDto.getRequestModeration());
        }
        return event;
    }

    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .location(LocationMapper.toLocationDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .build();
    }

    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .build();
    }
}
