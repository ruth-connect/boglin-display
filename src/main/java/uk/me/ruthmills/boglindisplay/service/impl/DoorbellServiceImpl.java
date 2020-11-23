package uk.me.ruthmills.boglindisplay.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.me.ruthmills.boglindisplay.service.BuzzerService;
import uk.me.ruthmills.boglindisplay.service.DoorbellService;

@Service
public class DoorbellServiceImpl implements DoorbellService {

	@Autowired
	private BuzzerService buzzerService;

	@Override
	public void ringDoorbell() {
		buzzerService.setBuzzer(true);
		sleep(500);
		buzzerService.setBuzzer(false);
		sleep(500);
		buzzerService.setBuzzer(true);
		sleep(500);
		buzzerService.setBuzzer(false);
	}

	private void sleep(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException ex) {
		}
	}

	@Override
	public void driveDisconnected() {
		buzzerService.setBuzzer(true);
		sleep(250);
		buzzerService.setBuzzer(false);
		sleep(250);
		buzzerService.setBuzzer(true);
		sleep(250);
		buzzerService.setBuzzer(false);
		sleep(250);
		buzzerService.setBuzzer(true);
		sleep(250);
		buzzerService.setBuzzer(false);
		sleep(750);

		buzzerService.setBuzzer(true);
		sleep(250);
		buzzerService.setBuzzer(false);
		sleep(250);
		buzzerService.setBuzzer(true);
		sleep(250);
		buzzerService.setBuzzer(false);
		sleep(250);
		buzzerService.setBuzzer(true);
		sleep(250);
		buzzerService.setBuzzer(false);
	}
}
