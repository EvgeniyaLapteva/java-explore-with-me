package ru.practicum.ewm.mapper;

import ru.practicum.ewm.dto.LocationDto;
import ru.practicum.ewm.model.Location;

public class LocationMapper {

    public static LocationDto toLocationDto(Location location) {
        return LocationDto.builder()
                .lon(location.getLon())
                .lat(location.getLat())
                .build();
    }

    public static Location toLocation(LocationDto locationDto) {
        Location location = new Location();
        location.setLat(locationDto.getLat());
        location.setLon(location.getLon());
        return location;
    }
}
