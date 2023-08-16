package ru.practicum.ewm.dto.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.LocationDto;
import ru.practicum.ewm.dto.users.UserShortDto;
import ru.practicum.ewm.model.enums.EventState;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {

    @NotBlank
    private String annotation;

    @NotBlank
    private CategoryDto category;

    private Integer confirmedRequests;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotBlank
    private LocalDateTime eventDate;

    private Long id;

    @NotBlank
    private UserShortDto initiator;

    @NotBlank
    private LocationDto location;

    @NotBlank
    private Boolean paid;

    private Integer participantLimit;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;

    private Boolean requestModeration;

    private EventState state;

    @NotBlank
    private String title;

    private Long views;
}
