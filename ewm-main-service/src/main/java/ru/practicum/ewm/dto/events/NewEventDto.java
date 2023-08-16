package ru.practicum.ewm.dto.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.dto.LocationDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {

    @NotBlank
    @Length(min = 20, max = 2000)
    private String annotation;

    @NotNull
    private Long category;

    @NotBlank
    @Length(min = 20, max = 7000)
    private String description;

    @NotBlank
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //не раньше, чем через два часа от текущего времени
    private LocalDateTime eventDate;

    @NotBlank
    private LocationDto location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    @NotBlank
    @Length(min = 3, max = 120)
    private String title;
}
