package ru.practicum.ewm.dto.compilations;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.validation.Create;
import ru.practicum.ewm.validation.Update;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
public class NewCompilationDto {

    private List<Long> events;

    private Boolean pinned;

    @Length(min = 1, max = 50, groups = {Create.class, Update.class})
    @NotBlank(groups = Create.class)
    private String title;

    public Boolean getPinned() {
        if (pinned == null) {
            pinned = false;
        }
        return pinned;
    }
}
