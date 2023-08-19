package ru.practicum.ewm.mapper;

import ru.practicum.ewm.dto.LocationDto;
import ru.practicum.ewm.model.Location;

public class LocationMapper {

    public static LocationDto toLocationDto(Location location) {
        return LocationDto.builder()
                .id(location.getId())
                .lon(location.getLon())
                .lat(location.getLat())
                .build();
    }

    public static Location toLocation(LocationDto locationDto) {
        Location location = new Location();
        location.setId(locationDto.getId());
        location.setLat(locationDto.getLat());
        location.setLon(locationDto.getLon());
        return location;
    }
}
