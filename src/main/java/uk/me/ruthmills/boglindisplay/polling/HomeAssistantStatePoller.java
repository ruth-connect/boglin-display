package uk.me.ruthmills.boglindisplay.polling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import uk.me.ruthmills.boglindisplay.service.HomeAssistantQueryService;

@Component
public class HomeAssistantStatePoller {

	@Autowired
	private HomeAssistantQueryService homeAssistantQueryService;

	private final Logger logger = LoggerFactory.getLogger(HomeAssistantStatePoller.class);

	@Scheduled(cron = "0 */1 * * * *")
	public void tick() {
		try {
			homeAssistantQueryService.querySensor("sensor.big_bedroom_filtered_temperature");
		} catch (Exception ex) {
			logger.error("Exception in poller thread", ex);
		}
	}
}
