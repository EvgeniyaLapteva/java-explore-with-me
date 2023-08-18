package ru.practicum.ewm.mapper;

import ru.practicum.ewm.dto.participationRequest.ParticipationRequestDto;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.ParticipationRequest;
import ru.practicum.ewm.model.User;

public class ParticipationRequestMapper {

    public static ParticipationRequestDto toRequestDto(ParticipationRequest request) {
        return ParticipationRequestDto.builder()
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .id(request.getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }

    public static ParticipationRequest toRequest(ParticipationRequestDto requestDto, User requester, Event event) {
        ParticipationRequest request = new ParticipationRequest();
        request.setId(requestDto.getId());
        request.setCreated(requestDto.getCreated());
        request.setEvent(event);
        request.setRequester(requester);
        request.setStatus(requestDto.getStatus());
        return request;
    }
}
