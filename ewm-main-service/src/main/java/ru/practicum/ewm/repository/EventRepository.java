package ru.practicum.ewm.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.enums.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByCategoryId(Long catId);

    List<Event> findAllByInitiatorId(Long userId, PageRequest page);

    @Query("select e from Event e " +
            "where (coalesce(:users, null) is  null or e.initiator.id in :users) " +
            "and (coalesce(:states, null) is null or e.state in :states) " +
            "and (coalesce(:categories, null) is null or e.category.id in :categories) " +
            "and (coalesce(:rangeStart, null) is null or e.eventDate >= :rangeStart) " +
            "and (coalesce(:rangeEnd, null) is null or e.eventDate <= :rangeEnd)")
    List<Event> getAllEventsByAdmin(@Param("users") List<Long> users, @Param("states") List<EventState> states,
                                    @Param("categories") List<Long> categories,
                                    @Param("rangeStart") LocalDateTime rangeStart,
                                    @Param("rangeEnd") LocalDateTime rangeEnd, PageRequest page);

    @Query("select e from Event as e " +
            "where (e.state = 'PUBLISHED') " +
            "and (coalesce(:text, null) is null or (lower(e.annotation) like lower(concat('%', :text, '%')) " +
            "or lower(e.description) like lower(concat('%', :text, '%')))) " +
            "and (coalesce(:categories, null) is null or e.category.id in :categories) " +
            "and (coalesce(:paid, null) is null or e.paid = :paid) " +
            "and (coalesce(:rangeStart, null) is null or e.eventDate >= :rangeStart) " +
            "and (coalesce(:rangeEnd, null) is null or e.eventDate <= :rangeEnd) " +
            "and (e.confirmedRequests < e.participantLimit or :onlyAvailable = false) " +
            "group by e.id " +
            "order by lower(:sort) asc")
    List<Event> getEventsPublic(@Param("text") String text, @Param("categories") List<Long> categories,
                                @Param("paid") Boolean paid, @Param("rangeStart") LocalDateTime rangeStart,
                                @Param("rangeEnd") LocalDateTime rangeEnd,
                                @Param("onlyAvailable") Boolean onlyAvailable, @Param("sort") String sort,
                                PageRequest page);
}
