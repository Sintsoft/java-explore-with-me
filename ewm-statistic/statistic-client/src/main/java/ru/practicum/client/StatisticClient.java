package ru.practicum.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.StatHitRequestDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class StatisticClient extends BaseClient {



    public StatisticClient(@Value("${ewm.statistic-url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public void hitUri(StatHitRequestDTO dto) {
        post("/hit", dto);
    }

    public ResponseEntity<Object> getStatistic(
            LocalDateTime start,
            LocalDateTime end,
            boolean unique,
            List<String> uris) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "unique", unique,
                "uris", uris
        );
        return get("/stats", parameters);
    }
}
