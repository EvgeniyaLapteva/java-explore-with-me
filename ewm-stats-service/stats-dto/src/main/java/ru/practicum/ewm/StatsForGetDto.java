package ru.practicum.ewm;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatsForGetDto {

    public StatsForGetDto(String app, String uri, Long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }

    private String app;

    private String uri;

    private Long hits;
}
