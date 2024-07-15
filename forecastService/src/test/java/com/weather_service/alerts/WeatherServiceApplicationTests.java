package com.weather_service.alerts;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.weather.forecast.controller.ForecastController;
import com.weather.forecast.dto.WeatherForecastResponse;
import com.weather.forecast.service.WeatherService;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class WeatherServiceApplicationTests {

	@Mock
	private WeatherService weatherService;

	@InjectMocks
	private ForecastController forecastController;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testForecastEndpoint() {
		String wfo = "wfo";
		String gridX = "gridX";
		String gridY = "gridY";

		WeatherForecastResponse mockWeatherResponse = new WeatherForecastResponse(); // Create mock response data

		when(weatherService.getDailyForecast(wfo, gridX, gridY)).thenReturn(Mono.just(mockWeatherResponse));

		Mono<WeatherForecastResponse> responseMono = forecastController.getForecast(wfo, gridX, gridY);

		StepVerifier.create(responseMono).expectNext(mockWeatherResponse).verifyComplete();
	}

}
