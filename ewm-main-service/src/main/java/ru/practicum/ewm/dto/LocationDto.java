package ru.practicum.ewm.dto;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;

@Data
@Builder
public class LocationDto {

    private Double lat;

    private Double lon;
}
