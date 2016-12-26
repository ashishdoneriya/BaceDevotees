package bace.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class LoginController {
	
	@RequestMapping(value = "admin", method = RequestMethod.GET)
	public String list() {
		return "redirect:/";
	}

}
