package ru.practicum.ewm.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.ewm.HitForPostDto;
import ru.practicum.ewm.StatsForGetDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static ru.practicum.ewm.ConstantsForDto.FOR_FORMATTER;

@Service
public class StatsClient {

    @Value("${stats-server.url}")
    private String serverUrl;

    private final RestTemplate restTemplate;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FOR_FORMATTER);

    @Autowired
    public StatsClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void createHit(HitForPostDto hitDto) {
        restTemplate.postForLocation(serverUrl.concat("/hit"), hitDto);
    }

    public List<StatsForGetDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        Map<String, Object> parameters = new HashMap<>(Map.of(
                "start", start.format(formatter),
                "end", end.format(formatter),
                "unique", unique));
        if (uris != null && !uris.isEmpty()) {
            parameters.put("uris", String.join(",", uris));
        }
        StatsForGetDto[] response = restTemplate.getForObject(
                serverUrl.concat("/stats?start={start}&end={end}&uris={uris}&unique={unique}"),
                StatsForGetDto[].class, parameters);
        return Objects.isNull(response) ? List.of() : List.of(response);
    }
}
