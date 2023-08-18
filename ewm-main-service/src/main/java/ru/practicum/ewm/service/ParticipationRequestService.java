package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.participationRequest.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {

    List<ParticipationRequestDto> getRequestsByUserId(Long userId);

    ParticipationRequestDto createParticipationRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelParticipationRequestStatus(Long userId, Long requestId);
}
