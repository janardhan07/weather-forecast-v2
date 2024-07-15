package com.weather.forecast.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weather.forecast.dto.WeatherForecastResponse;
import com.weather.forecast.service.WeatherService;

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
@RequestMapping("/gridpoints")
@Slf4j
public class ForecastController {

	@Autowired
	private WeatherService weatherService;

	/**
	 * @Description - This api is to retrieve the forecast for the given Forecast Office Id and co-ordinates
	 * @param officeId
	 * @param gridX
	 * @param gridY
	 * @return
	 */
	@GetMapping("/{officeId}/{gridX},{gridY}/forecast")
	@Tag(name = "Weather Forecast", description = "To get the forecast for the grid location")
	@Operation(description = "Get daily grid forecast", parameters = {
			@Parameter(name = "officeId", in = ParameterIn.PATH, required = true, description = "Office location"),
			@Parameter(name = "gridX", in = ParameterIn.PATH, required = true, description = "Grid latitude co-ordinates"),
			@Parameter(name = "gridY", in = ParameterIn.PATH, required = true, description = "Grid longitude co-ordinates") 
			})
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Get the weather forecast", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = WeatherForecastResponse.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) }),
			@ApiResponse(responseCode = "500", description = "Server Error", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) }) })
	public Mono<WeatherForecastResponse> getForecast(
			@PathVariable("officeId") @Pattern(regexp = "^[a-zA-Z]+$", message = "officeId cannot be blank") String officeId,
			@PathVariable("gridX") @Positive(message = "GridX cannot be blank") String gridX,
			@PathVariable("gridY") @Positive(message = "GridY cannot be blank") String gridY) 
			{
		log.info("Weather forest information returned.");
		return weatherService.getDailyForecast(officeId, gridX, gridY);
	}
}
