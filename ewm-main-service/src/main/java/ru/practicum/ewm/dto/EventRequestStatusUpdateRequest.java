package ru.practicum.ewm.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EventRequestStatusUpdateRequest {

    private List<Long> requestIds;

    private List<String> status;
}