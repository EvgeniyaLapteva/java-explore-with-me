package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.participationRequest.ParticipationRequestDto;
import ru.practicum.ewm.exception.model.ConditionsAreNotMetException;
import ru.practicum.ewm.exception.model.ObjectNotFoundException;
import ru.practicum.ewm.mapper.ParticipationRequestMapper;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.ParticipationRequest;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model.enums.EventState;
import ru.practicum.ewm.model.enums.RequestStatus;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.ParticipationRequestRepository;
import ru.practicum.ewm.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    private final ParticipationRequestRepository repository;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getRequestsByUserId(Long userId) {
        getUser(userId);
        List<ParticipationRequest> requests = repository.findByRequesterId(userId);
        log.info("Получили список заявок на участие пользователя id = {} в чужих событиях", userId);
        return requests.stream()
                .map(ParticipationRequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto createParticipationRequest(Long userId, Long eventId) {
        User user = getUser(userId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Event with id=" + eventId + " was not found"));
        if (Objects.equals(userId, event.getInitiator().getId())) {
            throw new ConditionsAreNotMetException("Initiator of event has id = " + event.getInitiator().getId() +
                    " and user id is " + userId);
        }
        if (event.getParticipantLimit() <= event.getConfirmedRequests() && event.getParticipantLimit() != 0) {
            throw new ConditionsAreNotMetException("Exceeded the limit of participants");
        }
        if (event.getState() != EventState.PUBLISHED) {
            throw new ConditionsAreNotMetException("The event id = " + eventId + " has not published yet");
        }

        ParticipationRequest request = new ParticipationRequest();
        request.setRequester(user);
        request.setEvent(event);
        request.setCreated(LocalDateTime.now());
        request.setStatus(RequestStatus.PENDING);
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
            request = repository.save(request);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1L);
            log.info("Создали и подтвердили запрос пользователя id = {} на участие в событии id = {}", userId, eventId);
            return ParticipationRequestMapper.toRequestDto(request);
        }
        request = repository.save(request);
        log.info("Создали запрос пользователя id = {} на участие в событии id = {}", userId, eventId);
        return ParticipationRequestMapper.toRequestDto(request);
    }

    @Override
    public ParticipationRequestDto cancelParticipationRequestStatus(Long userId, Long requestId) {
        getUser(userId);
        ParticipationRequest request = repository.findById(requestId)
                .orElseThrow(() -> new ObjectNotFoundException("Request with id = " + requestId + " was not found"));
        request.setStatus(RequestStatus.CANCELED);
        ParticipationRequest canceledRequest = repository.save(request);
        log.info("Пользователь id = {} отменил заявку id = {}", userId, requestId);
        return ParticipationRequestMapper.toRequestDto(canceledRequest);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь" +
                        "с id = " + userId + " не найден"));
    }
}
