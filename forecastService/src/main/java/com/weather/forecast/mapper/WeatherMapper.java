package com.weather.forecast.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import com.weather.forecast.dto.DailyForecast;
import com.weather.forecast.dto.GridForecast;
import com.weather.forecast.dto.Period;
import com.weather.forecast.dto.WeatherForecastResponse;

@Component
public class WeatherMapper {

	private final ModelMapper modelMapper;

	public WeatherMapper() {
		this.modelMapper = new ModelMapper();

		Converter<Integer, Double> fahrenheitToCelsiusConverter = new Converter<Integer, Double>() {
			@Override
			public Double convert(MappingContext<Integer, Double> context) {
				Integer fahrenheit = context.getSource();
				if (fahrenheit == null) {
					return null;
				}
				return Double.valueOf(String.format("%.2f", (fahrenheit - 32) * 5.0 / 9.0));

			}
		};
		this.modelMapper.typeMap(Period.class, DailyForecast.class)
				.addMapping(Period::getName, DailyForecast::setDayName)
				.addMapping(Period::getShortForecast, DailyForecast::setForecastBlurp)
				.addMappings(mapper -> mapper.using(fahrenheitToCelsiusConverter).map(Period::getTemperature,
						DailyForecast::setTempHighCelsius));

	}

	/**
	 * @Desc - Convert the response from downstream response to API response
	 * @param gridForecast
	 * @return WeatherForecastResponse
	 */
	public WeatherForecastResponse toForecastResponse(GridForecast gridForecast) {
		List<DailyForecast> dailyForecasts = gridForecast.getProperties().getPeriods().stream()
				.map(period -> modelMapper.map(period, DailyForecast.class)).collect(Collectors.toList());
		return WeatherForecastResponse.builder().daily(dailyForecasts).build();
	}
}
