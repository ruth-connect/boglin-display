package uk.me.ruthmills.boglindisplay.service.impl;

import java.math.BigDecimal;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.me.ruthmills.boglindisplay.model.BoglinDisplayType;
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

	private BoglinDisplayType displayType;

	private static final Logger logger = LoggerFactory.getLogger(HomeAssistantStateServiceImpl.class);

	@PostConstruct
	public void initialise() {
		insideAirTemperature = "   ";
		insideAirHumidity = "   ";
		outsideAirTemperature = "   ";
		outsideAirHumidity = "   ";
		outsideAirPressure = "   ";

		displayType = BoglinDisplayType.INSIDE_AIR_TEMPERATURE;
	}

	@Override
	public String getDisplayName() {
		switch (displayType) {
		case INSIDE_AIR_TEMPERATURE:
			return "iAt";
		case INSIDE_AIR_HUMIDITY:
			return "iAh";
		case OUTSIDE_AIR_TEMPERATURE:
			return "oAt";
		case OUTSIDE_AIR_HUMIDITY:
			return "oAh";
		case OUTSIDE_AIR_PRESSURE:
			return "bAr";
		default:
			return "   ";
		}
	}

	@Override
	public String getDisplayValue() {
		switch (displayType) {
		case INSIDE_AIR_TEMPERATURE:
			return insideAirTemperature;
		case INSIDE_AIR_HUMIDITY:
			return insideAirHumidity;
		case OUTSIDE_AIR_TEMPERATURE:
			return outsideAirTemperature;
		case OUTSIDE_AIR_HUMIDITY:
			return outsideAirHumidity;
		case OUTSIDE_AIR_PRESSURE:
			return outsideAirPressure;
		default:
			return "   ";
		}
	}

	public void cycleDisplayType() {
		if (displayType.equals(BoglinDisplayType.INSIDE_AIR_TEMPERATURE)) {
			displayType = BoglinDisplayType.OUTSIDE_AIR_TEMPERATURE;
		} else if (displayType.equals(BoglinDisplayType.OUTSIDE_AIR_TEMPERATURE)) {
			displayType = BoglinDisplayType.INSIDE_AIR_HUMIDITY;
		} else if (displayType.equals(BoglinDisplayType.INSIDE_AIR_HUMIDITY)) {
			displayType = BoglinDisplayType.OUTSIDE_AIR_HUMIDITY;
		} else if (displayType.equals(BoglinDisplayType.OUTSIDE_AIR_HUMIDITY)) {
			displayType = BoglinDisplayType.OUTSIDE_AIR_PRESSURE;
		} else {
			displayType = BoglinDisplayType.INSIDE_AIR_TEMPERATURE;
		}
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
