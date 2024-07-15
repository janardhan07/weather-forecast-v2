package com.weather.forecast.dto;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlertsCountResponse {
	
	private String message;
	private int total;
	private int land;
	private int marine;
	private List<String> regions;

}
