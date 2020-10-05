package uk.me.ruthmills.boglindisplay.service.impl;

import java.math.BigDecimal;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.me.ruthmills.boglindisplay.service.HomeAssistantQueryService;
import uk.me.ruthmills.boglindisplay.service.HomeAssistantStateService;

@Service
public class HomeAssistantStateServiceImpl implements HomeAssistantStateService {

	private static final String INSIDE_AIR_TEMPERATURE = "sensor.big_bedroom_filtered_temperature";
	private static final String INSIDE_AIR_HUMIDITY = "sensor.big_bedroom_filtered_humidity";
	private static final String OUTSIDE_AIR_TEMPERATURE = "sensor.outside_filtered_temperature";
	private static final String OUTSIDE_AIR_HUMIDITY = "sensor.outside_filtered_humidity";
	private static final String OUTSIDE_AIR_PRESSURE = "sensor.outside_filtered_air_pressure";

	@Autowired
	private HomeAssistantQueryService homeAssistantQueryService;

	private String insideAirTemperature;
	private String insideAirHumidity;
	private String outsideAirTemperature;
	private String outsideAirHumidity;
	private String outsideAirPressure;

	private static final Logger logger = LoggerFactory.getLogger(HomeAssistantStateServiceImpl.class);

	@PostConstruct
	public void initialise() {
		insideAirTemperature = "   ";
		insideAirHumidity = "   ";
		outsideAirTemperature = "   ";
		outsideAirHumidity = "   ";
		outsideAirPressure = "   ";
	}

	@Override
	public String getInsideAirTemperature() {
		return insideAirTemperature;
	}

	@Override
	public String getInsideAirHumidity() {
		return insideAirHumidity;
	}

	@Override
	public String getOutsideAirTemperature() {
		return outsideAirTemperature;
	}

	@Override
	public String getOutsideAirHumidity() {
		return outsideAirHumidity;
	}

	@Override
	public String getOutsideAirPressure() {
		return outsideAirPressure;
	}

	@Override
	public void updateStates() {
		insideAirTemperature = getTemperatureOrHumidity(INSIDE_AIR_TEMPERATURE);
		insideAirHumidity = getTemperatureOrHumidity(INSIDE_AIR_HUMIDITY);
		outsideAirTemperature = getTemperatureOrHumidity(OUTSIDE_AIR_TEMPERATURE);
		outsideAirHumidity = getTemperatureOrHumidity(OUTSIDE_AIR_HUMIDITY);
		outsideAirPressure = getPressure(OUTSIDE_AIR_PRESSURE);
	}

	private String getTemperatureOrHumidity(String sensor) {
		String state = homeAssistantQueryService.querySensor(sensor);
		BigDecimal stateBigDecimal = new BigDecimal(Math.round(Float.parseFloat(state) * 10));
		state = stateBigDecimal.divide(new BigDecimal(10)).toString();
		if (state.indexOf(".") < 0) {
			state += ".0";
		}
		if (Float.parseFloat(state) < 10) {
			state = " " + state;
		}

		logger.info("Formatted " + sensor + " as: " + state);
		return state;
	}

	private String getPressure(String sensor) {
		String state = homeAssistantQueryService.querySensor(sensor);
		state = Integer.valueOf(Math.round(Float.parseFloat(state) / 10)).toString();
		if (Integer.parseInt(state) < 100) {
			state = " " + state;
		}

		logger.info("Formatted " + sensor + " as: " + state);
		return state;
	}
}
