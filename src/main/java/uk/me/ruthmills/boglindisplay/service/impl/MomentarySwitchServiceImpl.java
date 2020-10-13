package uk.me.ruthmills.boglindisplay.service.impl;

import static com.pi4j.io.gpio.PinPullResistance.PULL_DOWN;
import static com.pi4j.io.gpio.PinState.LOW;
import static com.pi4j.io.gpio.RaspiPin.GPIO_06;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;

import uk.me.ruthmills.boglindisplay.service.GotekDisplayService;
import uk.me.ruthmills.boglindisplay.service.HomeAssistantStateService;
import uk.me.ruthmills.boglindisplay.service.MomentarySwitchService;

@Service
public class MomentarySwitchServiceImpl implements MomentarySwitchService {

	@Autowired
	private HomeAssistantStateService homeAssistantStateService;

	@Autowired
	private GotekDisplayService gotekDisplayService;

	private volatile GpioController gpio;
	private volatile GpioPinDigitalInput momentarySwitch;
	private volatile boolean shutdown;
	private volatile int countdown;

	private final Logger logger = LoggerFactory.getLogger(MomentarySwitchServiceImpl.class);

	@PostConstruct
	public void initialise() {
		gpio = GpioFactory.getInstance();
		momentarySwitch = gpio.provisionDigitalInputPin(GPIO_06, "Momentary Switch", PULL_DOWN);
		momentarySwitch.setShutdownOptions(true, LOW);

		Runnable runnable = new MomentarySwitchRunnable();
		Thread thread = new Thread(runnable);
		thread.start();
	}

	@PreDestroy
	public void shutdown() {
		shutdown = true;
	}

	@Override
	public boolean isShowingDisplayName() {
		return countdown > 0;
	}

	private class MomentarySwitchRunnable implements Runnable {

		@Override
		public void run() {
			while (!shutdown) {
				try {
					if (momentarySwitch.isHigh()) {
						homeAssistantStateService.cycleDisplayType();
						gotekDisplayService.displayText(homeAssistantStateService.getDisplayName());
						countdown = 50;

						while (momentarySwitch.isHigh()) {
							Thread.sleep(20);
						}
					}
					Thread.sleep(20);
					if (countdown > 0) {
						countdown--;
						if (countdown == 0) {
							gotekDisplayService.displayText(homeAssistantStateService.getDisplayValue());
						}
					}
				} catch (Exception ex) {
					logger.error("Exception in momentary switch thread", ex);
				}
			}

			gpio.shutdown();
		}
	}
}
