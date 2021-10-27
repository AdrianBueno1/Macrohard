package es.udc.fi.dc.fd.controller.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.udc.fi.dc.fd.controller.dto.AdPreviewDto;
import es.udc.fi.dc.fd.controller.dto.RateDto;
import es.udc.fi.dc.fd.controller.util.AdPreviewConversor;
import es.udc.fi.dc.fd.controller.util.FollowerConversor;
import es.udc.fi.dc.fd.model.persistence.Ad;
import es.udc.fi.dc.fd.model.persistence.Follower;
import es.udc.fi.dc.fd.model.persistence.User;
import es.udc.fi.dc.fd.repository.AdRepository;
import es.udc.fi.dc.fd.repository.UserRepository;
import es.udc.fi.dc.fd.service.AdService;
import es.udc.fi.dc.fd.service.FavService;
import es.udc.fi.dc.fd.service.FollowService;
import es.udc.fi.dc.fd.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	private UserRepository userRepository;

	private FollowService followService;

	private UserService userService;

	private AdRepository adRepository;

	private FavService favService;

	private AdService adService;

	@ModelAttribute("rateDto")
	public RateDto rateDto() {
		return new RateDto();
	}

	@Autowired
	public UserController(UserRepository userRepository, FollowService followService, UserService userService,
			AdRepository adRepository, FavService favService, AdService adService) {
		super();
		this.userRepository = userRepository;
		this.followService = followService;
		this.userService = userService;
		this.adRepository = adRepository;
		this.favService = favService;
		this.adService = adService;
	}

	@GetMapping("/myProfile")
	public String showProfile(ModelMap model, Authentication authentication) {

		User act_user = getActualUser();

		BigDecimal bd = new BigDecimal(act_user.getRate()).setScale(1, BigDecimal.ROUND_HALF_UP);
		act_user.setRate(bd.floatValue());

		model.addAttribute("my_id", act_user.getId());
		model.addAttribute("user", act_user);
		model.addAttribute("percent_rate", (act_user.getRate() * 100) / 5);

		return "profile";
	}

	@GetMapping("/{userId}")
	public String showUserProfile(@PathVariable("userId") long userId, ModelMap model, Authentication authentication) {

		User act_user = getActualUser();

		User user = userRepository.findById(userId).get();

		BigDecimal bd = new BigDecimal(user.getRate()).setScale(1, BigDecimal.ROUND_HALF_UP);
		user.setRate(bd.floatValue());

		int rating = -1;

		model.addAttribute("user", user);
		model.addAttribute("my_id", act_user.getId());
		model.addAttribute("percent_rate", (user.getRate() * 100) / 5);

		rating = userService.getRating(act_user.getId(), userId);

		if (rating != -1) {
			model.addAttribute("rated", true);
			model.addAttribute("my_rating", rating);
		} else {
			model.addAttribute("rated", false);
		}

		return "profile";
	}

	@GetMapping("/following")
	public String showFollowing(ModelMap model, Authentication authentication) {

		User act_user = getActualUser();

		List<Follower> following = followService.findByFollowerPK_User(act_user);

		model.addAttribute("following", FollowerConversor.fromFollowerToUserList(following));

		return "following_list";
	}

	@GetMapping("/following/list")
	public String showFollowingAdList(ModelMap model, Authentication authentication) {

		User act_user = getActualUser();

		List<Follower> following = followService.findByFollowerPK_User(act_user);

		List<AdPreviewDto> ads = AdPreviewConversor.fromAdListToAdPreviewList(
				adRepository.findByUserIdIn(FollowerConversor.fromFollowerToIdList(following)));

		model.addAttribute("my_id", act_user.getId());
		model.addAttribute("userAds", ads);
		model.addAttribute("favs", favService.showFavs(act_user.getUserName()));
		model.addAttribute("following",
				FollowerConversor.fromFollowerToIdList(followService.findByFollowerPK_User(act_user)));

		return "userList_page";
	}

	@PostMapping("/unfollow/{id}")
	public String unfollow(@PathVariable("id") long id, Authentication authentication, HttpServletRequest request) {

		User act_user = getActualUser();

		User followed = userRepository.findById(id).get();

		followService.removeFollow(act_user.getUserName(), followed.getUserName());

		return "redirect:" + request.getHeader("Referer");
	}

	@PostMapping("/follow/{id}")
	public String follow(@PathVariable("id") long id, Authentication authentication, HttpServletRequest request) {

		User act_user = getActualUser();

		User followed = userRepository.findById(id).get();

		followService.follow(act_user.getUserName(), followed.getUserName());

		return "redirect:" + request.getHeader("Referer");
	}

	@PostMapping("/buy/{id}")
	public String buy(@PathVariable("id") long id, Authentication authentication, HttpServletRequest request) {

		Optional<User> user = userRepository.findByUserName(authentication.getName().toString());

		adService.buy(id, user.get().getId());

		return "redirect:" + request.getHeader("Referer");
	}

	@GetMapping("/{userId}/rate")
	public String showRatingForm(@PathVariable("userId") long userId, ModelMap model, Authentication authentication) {

		User act_user = getActualUser();

		User user = userRepository.findById(userId).get();
		BigDecimal bd = new BigDecimal(user.getRate()).setScale(1, BigDecimal.ROUND_HALF_UP);
		user.setRate(bd.floatValue());

		model.addAttribute("user", user);
		model.addAttribute("percent_rate", (user.getRate() * 100) / 5);

		int rating = userService.getRating(act_user.getId(), userId);
		if (rating != -1) {
			model.addAttribute("rated", true);
			model.addAttribute("my_rating", rating);
		} else {
			model.addAttribute("rated", false);
		}

		return "rate_form";
	}

	@PostMapping("/{userId}/rate")
	public String rate(@PathVariable("userId") long userId, Authentication authentication, HttpServletRequest request,
			@ModelAttribute("rating") @Valid RateDto rateDto) {

		User act_user = getActualUser();

		userService.rateUser(act_user.getId(), userId, rateDto.getValue());

		return "redirect:" + "/user/" + userId;
	}

	private User getActualUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return userRepository.findByUserName(auth.getName().toString()).get();
	}

	@GetMapping("/adsPurchased")
	public String showAdsPurchased(ModelMap model, Authentication authentication) {

		User act_user = getActualUser();

		List<Ad> adsPurchased = adService.showAdsPurchased(act_user.getId());

		model.addAttribute("my_id", act_user.getId());
		model.addAttribute("adsPurchased", adsPurchased);

		return "ads_purchased_list";
	}
}
