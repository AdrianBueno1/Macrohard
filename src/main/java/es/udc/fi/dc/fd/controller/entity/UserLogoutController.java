package es.udc.fi.dc.fd.controller.entity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/logout")
public class UserLogoutController {

	@GetMapping()
	public String logoutPage(ModelMap model) {
		return "logout";
	}

	@PostMapping()
	public String logout(ModelMap model, Authentication authentication) {

		SecurityContextHolder.getContext().setAuthentication(null);

		return "login";
	}

}
