package es.udc.fi.dc.fd.controller.entity;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class UserLoginController {

	@Autowired
	private MessageSource messageSource;

	@GetMapping()
	public String showLogin(ModelMap model) {
		return "login";
	}

	@GetMapping("/error")
	public String showLoginWithError(ModelMap model, Locale locale) {

		model.addAttribute("error", messageSource.getMessage("User.credentials.error", null, locale));
		return "login";
	}

}
