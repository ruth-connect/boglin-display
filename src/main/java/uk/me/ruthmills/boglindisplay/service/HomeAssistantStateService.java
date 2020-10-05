package uk.me.ruthmills.boglindisplay.service;

public interface HomeAssistantStateService {

	public String getDisplayName();

	public String getDisplayValue();

	public void cycleDisplayType();

	public void updateStates();
}
