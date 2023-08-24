package ru.practicum.ewm.dto.comments;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.dto.users.UserShortDto;

import java.time.LocalDateTime;

import static ru.practicum.ewm.ConstantsForDto.FOR_FORMATTER;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private Long id;

    private String text;

    private UserShortDto author;

    @JsonFormat(pattern = FOR_FORMATTER)
    private LocalDateTime publishedOn;

    private Long eventId;
}
