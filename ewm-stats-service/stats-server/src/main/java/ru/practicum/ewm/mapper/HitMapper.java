package ru.practicum.ewm.mapper;

import ru.practicum.ewm.HitForPostDto;
import ru.practicum.ewm.model.Hit;

public class HitMapper {

    public static HitForPostDto toHitDto(Hit hit) {
        return HitForPostDto.builder()
                .id(hit.getId())
                .app(hit.getApp())
                .uri(hit.getUri())
                .ip(hit.getIp())
                .timestamp(hit.getTimestamp())
                .build();
    }

    public static Hit toHit(HitForPostDto hitDto) {
        Hit hit = new Hit();
        hit.setId(hitDto.getId());
        hit.setApp(hitDto.getApp());
        hit.setUri(hitDto.getUri());
        hit.setIp(hitDto.getIp());
        hit.setTimestamp(hitDto.getTimestamp());
        return hit;
    }
}
