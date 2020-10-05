package uk.me.ruthmills.boglindisplay.service;

public interface HomeAssistantStateService {

	public String getInsideAirTemperature();

	public String getInsideAirHumidity();

	public String getOutsideAirTemperature();

	public String getOutsideAirHumidity();

	public String getOutsideAirPressure();

	public void updateStates();
}
