package ru.practicum.ewm.dto.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.dto.LocationDto;
import ru.practicum.ewm.model.enums.StateAction;

import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventDto {

    @Length(min = 20, max = 2000)
    private String annotation;

    private Long category;

    @Length(min = 20, max = 7000)
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future
    private LocalDateTime eventDate;

    private LocationDto location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private StateAction stateAction;

    @Length(min = 3, max = 120)
    private String title;
}
