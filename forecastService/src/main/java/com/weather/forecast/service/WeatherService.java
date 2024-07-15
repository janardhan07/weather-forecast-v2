package com.weather.forecast.service;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import com.weather.forecast.constants.Constants;
import com.weather.forecast.dto.GridForecast;
import com.weather.forecast.dto.WeatherForecastResponse;
import com.weather.forecast.exception.WeatherServiceException;
import com.weather.forecast.mapper.WeatherMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class WeatherService {

	@Autowired
	private WebClient webClient;
	
	@Autowired
	private WeatherMapper weatherMapper;

	@Value("${weather.api.baseurl}")
	private String baseUrl;

	@Value("${weather.api.gridendpoint}")
	private String gridEndPoint;

	public Mono<WeatherForecastResponse> getDailyForecast(String officeId, String gridX, String gridY) {
		try {
			URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl).path(gridEndPoint).buildAndExpand(officeId, gridX, gridY)
					.toUri();
			Mono<GridForecast> downStreamResponse = webClient.get().uri(uri).retrieve().bodyToMono(GridForecast.class)
					.switchIfEmpty(Mono.error(new WeatherServiceException(Constants.DOWN_STREAM_NO_DATA_ERROR_MSG)))
					.onErrorMap(WebClientResponseException.class,
							ex -> new WeatherServiceException(Constants.DOWN_STREAM_ERROR_MSG, ex));
			return downStreamResponse.map(gridForecast -> weatherMapper.toForecastResponse(gridForecast));
		} catch (Exception e) {
			log.error(Constants.EXCEPTION_ERROR_MSG, e);
			throw new WeatherServiceException(Constants.DOWN_STREAM_ERROR_MSG, e);
		}
	}

}
