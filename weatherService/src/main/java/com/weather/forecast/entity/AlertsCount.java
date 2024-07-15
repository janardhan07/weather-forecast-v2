package com.weather.forecast.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "alerts_count")
public class AlertsCount {
	
	@Id
    @GeneratedValue(generator = "uuid")
	private long id;
	private int total;
	private int land;
	private int marine;
	private List<String> regions;

}
