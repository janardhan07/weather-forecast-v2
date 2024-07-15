package com.weather.forecast.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.weather.forecast.entity.AlertsCount;

@Repository
public interface WeatherRepository extends CrudRepository<AlertsCount, Long>{

	public AlertsCount save(AlertsCount alertsRes);

}
