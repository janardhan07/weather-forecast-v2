package com.weather.forecast.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlertsCountResponseDTO {

	private int total;
	private int land;
	private int marine;
	private Map<String, Integer> regions;
	private Map<String, Integer> areas;
	private Map<String, Integer> zones;
}
