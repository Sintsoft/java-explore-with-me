package ru.practicum.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.StatHitRequestDTO;
import ru.practicum.dto.StatUriHitsResponseDTO;
import ru.practicum.utility.commons.Constants;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class StatisticClient {

    private final RestTemplate rest;

    private final String serverUrl;

    public StatisticClient(@Value("${ewm.statistic-url}") String serverUrl, RestTemplateBuilder builder) {
        this.serverUrl = serverUrl;
        this.rest =
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build();

    }

    public void hitUri(StatHitRequestDTO dto) {
        postHitToServer("/hit", dto);
    }

    public List<StatUriHitsResponseDTO> getStatistic(
            LocalDateTime start,
            LocalDateTime end,
            boolean unique,
            List<String> uris) {
        Map<String, Object> parameters = Map.of(
                "start", start.format(Constants.DATE_TIME_FORMAT),
                "end", end.format(Constants.DATE_TIME_FORMAT),
                "unique", unique
        );
        return getStatsFromServer("/stats", parameters, uris);
    }

    private <T> void postHitToServer(String path, @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, null);
        ResponseEntity<Object> ewmResponse;
        try {
            ewmResponse = rest.exchange(path, HttpMethod.POST, requestEntity, Object.class);
        } catch (HttpStatusCodeException e) {
            log.warn("Unsuccesfull statisyic save");
        }
    }

    private List<StatUriHitsResponseDTO> getStatsFromServer(
            String path,
            @Nullable Map<String, Object> parameters,
            List<String> uris) {
        try {
            if (parameters != null) {
                ObjectMapper mapper = new ObjectMapper();
                String uriString = serverUrl + path + "?start={start}&end={end}&unique={unique}&"
                        + String.join("&", uris.stream()
                        .map(uri -> "uris=" + uri).collect(Collectors.toList()));
                log.debug(serverUrl + path);
                ResponseEntity<Object[]> resp = rest.getForEntity(
                        uriString,
                        Object[].class,
                        parameters);
                Object[] objects = resp.getBody();
                List<StatUriHitsResponseDTO> dtos = Arrays.stream(objects)
                        .map(object -> mapper.convertValue(object, StatUriHitsResponseDTO.class))
                        .collect(Collectors.toList());
                return dtos;
            }
            return List.of();
        } catch (HttpStatusCodeException e) {
            log.warn("Can't get statistics, cause: " + e.getMessage());
            throw new RuntimeException("Can't get statistics, cause: " + e.getMessage());
        } catch (Exception e) {
            log.warn("Can't get statistics, cause: " + e.getMessage());
            throw new RuntimeException("Can't get statistics, cause: " + e.getMessage());
        }
    }
}
