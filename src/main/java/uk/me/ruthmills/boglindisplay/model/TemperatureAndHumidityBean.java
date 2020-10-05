package uk.me.ruthmills.boglindisplay.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TemperatureAndHumidityBean {

	private BigDecimal temperature;
	private BigDecimal humidity;
	private LocalDateTime readTime;

	public BigDecimal getTemperature() {
		return temperature;
	}

	public void setTemperature(BigDecimal temperature) {
		this.temperature = temperature;
	}

	public BigDecimal getHumidity() {
		return humidity;
	}

	public void setHumidity(BigDecimal humidity) {
		this.humidity = humidity;
	}

	public LocalDateTime getReadTime() {
		return readTime;
	}

	public void setReadTime(LocalDateTime readTime) {
		this.readTime = readTime;
	}
}
