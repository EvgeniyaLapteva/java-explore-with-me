package ru.practicum.ewm.dto.compilations;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.dto.events.EventShortDto;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
public class CompilationDto {

    private List<EventShortDto> events;

    private Long id;

    //@NotBlank
    private Boolean pinned;

    //@NotBlank
    private String title;

    public Boolean getPinned() {
        if (pinned == null) {
            pinned = false;
        }
        return pinned;
    }
}
