package uk.me.ruthmills.boglindisplay.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import uk.me.ruthmills.boglindisplay.service.GotekDisplayService;

@Service
public class GotekDisplayServiceImpl implements GotekDisplayService {

	private static final String GOTEK_DISPLAY_PATH = "/home/pi/GotekLEDC68/TM1651";

	private static final Logger logger = LoggerFactory.getLogger(GotekDisplayServiceImpl.class);

	@Override
	public void displayText(String text) {
		try {
			ProcessBuilder processBuilder = new ProcessBuilder(GOTEK_DISPLAY_PATH, text);
			processBuilder.redirectErrorStream(true);
			Process process = processBuilder.start();
			process.waitFor();
		} catch (Exception ex) {
			logger.error("Exception", ex);
		}
	}
}
