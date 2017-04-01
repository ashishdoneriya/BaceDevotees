package bace.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {
	
	@Autowired
	ServletContext servletContext;
	
	@RequestMapping(value = "admin", method = RequestMethod.GET)
	public String list() {
		return "redirect:/";
	}
	
	@ResponseBody
	@RequestMapping(value = "/dologin", method = RequestMethod.POST)
	public String login(HttpServletRequest request) {
		String password = request.getParameter("password");
		if (password.equals(servletContext.getInitParameter("LoginPassword"))) {
			request.getSession().setAttribute("password", "as");
			return "success";
		}
		return "failed";
		
	}
	
	@ResponseBody
	@RequestMapping(value = "/dologout", method = RequestMethod.POST)
	public String logout(HttpServletRequest request) {
		request.getSession().removeAttribute("password");
		return "success";
		
	}

}
