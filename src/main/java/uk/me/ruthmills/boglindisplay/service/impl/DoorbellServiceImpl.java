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
		sleep();
		buzzerService.setBuzzer(false);
		sleep();
		buzzerService.setBuzzer(true);
		sleep();
		buzzerService.setBuzzer(false);
	}

	private void sleep() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException ex) {
		}
	}
}
