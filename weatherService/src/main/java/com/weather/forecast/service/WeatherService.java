package com.weather.forecast.service;

import java.net.URI;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.weather.forecast.constants.Constants;
import com.weather.forecast.dto.AlertsCountResponse;
import com.weather.forecast.dto.AlertsCountResponseDTO;
import com.weather.forecast.dto.WeatherForecastResponse;
import com.weather.forecast.entity.AlertsCount;
import com.weather.forecast.exception.WeatherServiceException;
import com.weather.forecast.repository.WeatherRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@Transactional
public class WeatherService {

	@Autowired
	private WebClient webClient;

	@Autowired
	private WeatherRepository weatherRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${weather.api.baseurl}")
	private String baseUrl;

	@Value("${weather.api.zonesendpoint}")
	private String zonesEndPoint;

	@Value("${weather.api.gridendpoint}")
	private String gridEndPoint;

	public AlertsCountResponse getAlerts() throws JsonProcessingException {
		UriComponents builder = UriComponentsBuilder.fromHttpUrl(baseUrl + zonesEndPoint).build();
		HttpEntity<String> requestEntity = new HttpEntity<>(null);
		try {
			ResponseEntity<AlertsCountResponseDTO> response = restTemplate.exchange(builder.toUriString(),
					HttpMethod.GET, requestEntity, AlertsCountResponseDTO.class);
			AlertsCountResponseDTO res = response.getBody();

//			To save the data into local database
			AlertsCount ac = weatherRepository.save(AlertsCount.builder().total(res.getTotal()).land(res.getLand())
					.marine(res.getMarine()).regions(new ArrayList(res.getRegions().keySet())).build());
			return AlertsCountResponse.builder().total(ac.getTotal()).marine(ac.getMarine()).land(ac.getLand())
					.regions(ac.getRegions()).message("Success!").build();
		} catch (Exception e) {
			throw new WeatherServiceException(Constants.DOWN_STREAM_ERROR_MSG, e);
		}

	}

	/**
	 * This method will call the down stream service to get the daily forecast
	 * 
	 * @param officeId
	 * @param gridX
	 * @param gridY
	 * @return
	 */
	public Mono<WeatherForecastResponse> getDailyForecast(String officeId, String gridX, String gridY) {
		URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl).path(gridEndPoint).buildAndExpand(officeId, gridX, gridY)
				.toUri();
		Mono<WeatherForecastResponse> downStreamResponse = webClient.get().uri(uri).retrieve()
				.bodyToMono(WeatherForecastResponse.class);
		log.info("Got the down stream response..");
		return downStreamResponse;
	}

}
