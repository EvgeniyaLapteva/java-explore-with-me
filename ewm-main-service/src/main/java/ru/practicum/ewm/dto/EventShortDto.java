package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {

    @NotBlank
    private String annotation;

    @NotBlank
    private CategoryDto category;

    private Long confirmedRequests;

    @NotBlank
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Long id;

    @NotBlank
    private UserShortDto initiator;

    @NotBlank
    private Boolean paid;

    @NotBlank
    private String title;

    private Long views;
}
