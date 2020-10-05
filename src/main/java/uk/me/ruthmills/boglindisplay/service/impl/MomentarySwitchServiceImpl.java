package uk.me.ruthmills.boglindisplay.service.impl;

import static com.pi4j.io.gpio.PinPullResistance.PULL_DOWN;
import static com.pi4j.io.gpio.PinState.LOW;
import static com.pi4j.io.gpio.RaspiPin.GPIO_06;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;

@Service
public class MomentarySwitchServiceImpl {

	private volatile GpioController gpio;
	private volatile GpioPinDigitalInput momentarySwitch;
	private volatile boolean shutdown;

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

	private class MomentarySwitchRunnable implements Runnable {

		@Override
		public void run() {
			while (!shutdown) {
				try {
					if (momentarySwitch.isHigh()) {
						logger.info("Momentary Switch pressed");

						while (momentarySwitch.isHigh()) {
							Thread.sleep(20);
						}
					}
					Thread.sleep(20);
				} catch (Exception ex) {
					logger.error("Exception in momentary switch thread", ex);
				}
			}

			gpio.shutdown();
		}
	}
}
