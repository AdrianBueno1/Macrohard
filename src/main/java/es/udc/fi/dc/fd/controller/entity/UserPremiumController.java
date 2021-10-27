package es.udc.fi.dc.fd.controller.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.udc.fi.dc.fd.model.persistence.User;
import es.udc.fi.dc.fd.repository.UserRepository;
import es.udc.fi.dc.fd.service.UserService;

@Controller
@RequestMapping("/premium")
public class UserPremiumController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	public UserPremiumController(UserService userService, UserRepository userRepository) {
		super();
		this.userService = userService;
		this.userRepository = userRepository;
	}

	@GetMapping
	public String showPremium(ModelMap model, Authentication authentication) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByUserName(auth.getName().toString()).get();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			model.addAttribute("user", user);
		}
		return "premiumUser";
	}

	@PostMapping
	public String addPremium(Authentication authentication) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByUserName(auth.getName().toString()).get();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			userService.becomePremium(user.getUserName());
		}
		return "redirect:/";
	}
}
