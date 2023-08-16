package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.events.EventFullDto;
import ru.practicum.ewm.dto.events.EventShortDto;
import ru.practicum.ewm.dto.events.NewEventDto;
import ru.practicum.ewm.dto.events.UpdateEventUserRequest;
import ru.practicum.ewm.dto.participationRequest.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.participationRequest.EventRequestStatusUpdateResult;
import ru.practicum.ewm.dto.participationRequest.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
public class PrivateController {

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getEventsByUserId(@PathVariable Long userId,
                                                 @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                 @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Запрос на получение списка событий пользовтеля id = {}", userId);
        return new ArrayList<>();
    }

    @PostMapping("/{userId}/events")
    public EventFullDto createEvent(@PathVariable Long userId, @RequestBody NewEventDto eventDto) {
        log.info("Запрос на создание события пользователем id = {}", userId);
        return null;
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEventById(@PathVariable Long userId, @PathVariable Long eventId) {
        //        Получение полной информации о событии добавленном текущим пользователем
        log.info("Запрос на получение информации о событии по id = {}", eventId);
        return null;
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEventById(@PathVariable Long userId, @PathVariable Long eventId,
                                        @RequestBody UpdateEventUserRequest eventDto) {
        log.info("Запрос на обновление события id = {}", eventDto);
//        изменить можно только отмененные события или события в состоянии ожидания модерации (Ожидается код ошибки 409)
//        дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента (Ожидается код ошибки 409)
        return  null;
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsByUserIdAndEventId(@PathVariable Long userId,
                                                                       @PathVariable Long eventId) {
        log.info("Запрос на получение списка заявок на участие в событии id = {} пользователя id = {}", eventId, userId);
        return  new ArrayList<>();
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequestsStatus(@PathVariable Long userId,
                                                               @PathVariable Long eventId,
                                                               @Valid @RequestBody EventRequestStatusUpdateRequest request) {
//        если для события лимит заявок равен 0 или отключена пре-модерация заявок, то подтверждение заявок не требуется
//        нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие (Ожидается код ошибки 409)
//        статус можно изменить только у заявок, находящихся в состоянии ожидания (Ожидается код ошибки 409)
//        если при подтверждении данной заявки, лимит заявок для события исчерпан, то все неподтверждённые заявки необходимо отклонить
        log.info("Запрос на обновление статусов заявок на участии в событии id = {} пользователя id = {}", eventId,
                userId);
        return null;
    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getRequestsByUserId(@PathVariable Long userId) {
        log.info("Запрос на получение списка заявок на участие пользователя id = {} на участие в чужих событиях", userId);
        return new ArrayList<>();
    }

    @PostMapping("/{userId}/requests")
    public ParticipationRequestDto createParticipationRequest(@PathVariable Long userId,
                                                              @RequestParam Long eventId) {
        log.info("Запрос на создание заявки на участие в событии id = {} пользователем id = {}", eventId, userId);
        return null;
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto updateParticipationRequestStatus(@PathVariable Long userId,
                                                                    @PathVariable Long requestId) {
        log.info("Запрос от пользователя id = {} на отмену участия в событии", userId);
        return null;
    }
}
