package ru.practicum.ewm.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.ewm.model.enums.RequestStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "participation_requests")
@Getter
@Setter
@ToString
public class ParticipationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    @ToString.Exclude
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    @ToString.Exclude
    private User requester;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestStatus status;
}
