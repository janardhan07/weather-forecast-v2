package com.weather.forecast.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.weather.forecast.dto.AlertsCountResponse;
import com.weather.forecast.dto.WeatherForecastResponse;
import com.weather.forecast.service.WeatherService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * This api is to retrieve alerts active count
 */
@RestController
@RequestMapping("/api/v1/weather")
@Slf4j
public class WeatherServiceController {

	@Autowired
	private WeatherService weatherService;

//	@GetMapping("/alertscount")
//	@Tag(name = "Weather Alerts", description = "To get the regions wise alerts count")
//	@CircuitBreaker(name = "getAlerts", fallbackMethod = "fallbackGetAlertsResponse")
//	public AlertsCountResponse getAlerts() throws JsonProcessingException {
//		return weatherService.getAlerts();
//	}
//
//	public AlertsCountResponse fallbackGetAlertsResponse(RuntimeException exception) {
//		log.error("Weather service is failed to connect!");
//		return new AlertsCountResponse().builder()
//				.message("Something went wrong to fetch alerts. Please try again after some time!").build();
//	}

	/**
	 * @Description - This api is to retrieve the forecast for the given Forecast
	 *              Office Id and co-ordinates
	 * @param wfo
	 * @param gridX
	 * @param gridY
	 * @return
	 */
	@GetMapping("/forecast/{wfo}/{gridX},{gridY}")
	@PreAuthorize("hasRole('ROLE_`USER')")
	@Tag(name = "Weather Forecast", description = "To get the forecast for the grid location")
	@Operation(description = "Get daily grid forecast", parameters = {
			@Parameter(name = "wfo", in = ParameterIn.PATH, required = true, description = "Office location"),
			@Parameter(name = "gridX", in = ParameterIn.PATH, required = true, description = "Grid latitude co-ordinates"),
			@Parameter(name = "gridY", in = ParameterIn.PATH, required = true, description = "Grid longitude co-ordinates") })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Get the weather forecast", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = WeatherForecastResponse.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) }),
			@ApiResponse(responseCode = "500", description = "Server Error", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) }) })
	@CircuitBreaker(name = "forecast", fallbackMethod = "fallbackGetForecast")
	public Mono<WeatherForecastResponse> getForecast(
			@PathVariable("wfo") @Pattern(regexp = "^[a-zA-Z]+$", message = "wfo cannot be blank") String wfo,
			@PathVariable("gridX") @Positive(message = "GridX cannot be blank") String gridX,
			@PathVariable("gridY") @Positive(message = "GridY cannot be blank") String gridY) {
		return weatherService.getDailyForecast(wfo, gridX, gridY);
	}

	public Mono<WeatherForecastResponse> fallbackGetForecast(String wfo, String gridX, String gridY,
			RuntimeException exception) {
		log.error("Weather Forecast service is failed to connect!");
		return Mono.fromCallable(() -> new WeatherForecastResponse().builder()
				.message("Something went wrong while fetching forecast. Try after some time!").build());
	}
}
