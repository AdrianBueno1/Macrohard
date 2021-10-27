package es.udc.fi.dc.fd.controller.entity;

import java.util.Locale;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.udc.fi.dc.fd.controller.dto.UserSignUpDto;
import es.udc.fi.dc.fd.model.exceptions.DuplicateEmailException;
import es.udc.fi.dc.fd.model.exceptions.DuplicateUserException;
import es.udc.fi.dc.fd.model.exceptions.IncorrectCreditCardException;
import es.udc.fi.dc.fd.model.persistence.User;
import es.udc.fi.dc.fd.service.UserService;

/**
 * Controller for the registration related options.
 * <p>
 * This serves as an adapter between the UI and the services layer.
 * 
 */

@Controller
@RequestMapping("/signUp")
public class UserSignUpController {

	/**
	 * User service.
	 */
	@Autowired
	private UserService userService;

	@Autowired
	private MessageSource messageSource;
	
	

	public UserSignUpController(UserService userService, MessageSource messageSource) {
		super();
		this.userService = userService;
		this.messageSource = messageSource;
	}

	@ModelAttribute("user")
	public UserSignUpDto userSignUpDto() {
		return new UserSignUpDto();
	}

	@GetMapping()
	public String showRegister(ModelMap model) {
		return "register_form";
	}

	@PostMapping()
	public String addUser(@ModelAttribute("user") @Valid UserSignUpDto userDto, final BindingResult errors,
			ModelMap model, final HttpServletResponse response, Locale locale) throws IncorrectCreditCardException {

		User user;

		if (errors.hasErrors()) {
			model.addAttribute("registerError", true);
			return "register_form";
		}

		if (!userDto.getConfirmEmail().equals(userDto.getEmail())) {
			errors.rejectValue("confirmEmail", "email_not_equal",
					messageSource.getMessage("User.email.equal", null, locale));
			return "register_form";
		}

		if (!userDto.getConfirmPassword().equals(userDto.getPassword())) {
			errors.rejectValue("confirmPassword", "password_not_equal",
					messageSource.getMessage("User.password.equal", null, locale));
			return "register_form";
		}

		user = new User();

		user.setEmail(userDto.getEmail().trim());
		user.setPassword(userDto.getPassword().trim());
		user.setFirstName(userDto.getFirstName().trim());
		user.setLastName(userDto.getLastName().trim());
		user.setUserName(userDto.getUsername().trim());
		user.setCity(userDto.getCity().trim());
		user.setCreditCard(userDto.getCreditCard());
		user.setRate(0);

		try {

			userService.signUp(user);

		} catch (DuplicateUserException e) {
			errors.rejectValue("username", "username_exists",
					messageSource.getMessage("User.username.exists", null, locale));
			return "register_form";

		} catch (DuplicateEmailException e) {
			errors.rejectValue("email", "email_exists", messageSource.getMessage("User.email.exists", null, locale));
			return "register_form";
		} catch (IncorrectCreditCardException e) {
			errors.rejectValue("creditCard", "creditCard_length",
					messageSource.getMessage("User.creditCard.lenght", null, locale));
		}

		return "redirect:/login";
	}

}