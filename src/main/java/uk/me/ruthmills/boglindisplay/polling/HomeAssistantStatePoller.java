package uk.me.ruthmills.boglindisplay.polling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import uk.me.ruthmills.boglindisplay.service.GotekDisplayService;
import uk.me.ruthmills.boglindisplay.service.HomeAssistantStateService;
import uk.me.ruthmills.boglindisplay.service.MomentarySwitchService;

@Component
public class HomeAssistantStatePoller {

	@Autowired
	private HomeAssistantStateService homeAssistantStateService;

	@Autowired
	private MomentarySwitchService momentarySwitchService;

	@Autowired
	private GotekDisplayService gotekDisplayService;

	private final Logger logger = LoggerFactory.getLogger(HomeAssistantStatePoller.class);

	@Scheduled(cron = "0 */1 * * * *")
	public void tick() {
		try {
			homeAssistantStateService.updateStates();
			if (!momentarySwitchService.isShowingDisplayName()) {
				gotekDisplayService.displayText(homeAssistantStateService.getDisplayValue());
			}
		} catch (Exception ex) {
			logger.error("Exception in poller thread", ex);
		}
	}
}
