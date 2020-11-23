package uk.me.ruthmills.boglindisplay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uk.me.ruthmills.boglindisplay.service.DoorbellService;

@Controller
@RequestMapping("/doorbell")
public class DoorbellController {

	@Autowired
	private DoorbellService doorbellService;

	@RequestMapping(value = "/ring", method = RequestMethod.POST)
	public void ringDoorbell() {
		doorbellService.ringDoorbell();
	}

	@RequestMapping(value = "/driveDisconnected", method = RequestMethod.POST)
	public void driveDisconnected() {
		doorbellService.driveDisconnected();
	}
}
