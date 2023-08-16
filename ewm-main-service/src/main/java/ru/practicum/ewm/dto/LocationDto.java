package ru.practicum.ewm.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class LocationDto {

    @NotBlank
    private Float lat;

    @NotBlank
    private Float lon;
}
