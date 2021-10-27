package es.udc.fi.dc.fd.controller.entity;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.udc.fi.dc.fd.controller.util.AdPreviewConversor;
import es.udc.fi.dc.fd.controller.util.FollowerConversor;
import es.udc.fi.dc.fd.model.persistence.Fav;
import es.udc.fi.dc.fd.model.persistence.User;
import es.udc.fi.dc.fd.repository.UserRepository;
import es.udc.fi.dc.fd.service.FavService;
import es.udc.fi.dc.fd.service.FollowService;

@Controller
@RequestMapping("/Fav")
public class FavController {

	@Autowired
	private FavService favService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FollowService followService;

	@Autowired
	public FavController(FavService favService, UserRepository userRepository, FollowService followService) {
		super();

		this.favService = favService;
		this.userRepository = userRepository;
		this.followService = followService;
	}

	@PostMapping()
	public String addFav(@RequestParam("adId") Long adId, Authentication authentication, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			Fav fav;
			fav = new Fav();
			fav.setUserName(auth.getName().toString());
			fav.setAdId(adId);
			favService.addFav(fav);
		}
		return "redirect:" + request.getHeader("Referer");
	}

	@PostMapping("/remove")
	public String removeFav(@RequestParam("adId") Long adId, Authentication authentication,
			HttpServletRequest request) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (!(auth instanceof AnonymousAuthenticationToken)) {
			favService.remove(adId, auth.getName().toString());
		}
		return "redirect:" + request.getHeader("Referer");
	}

	@PostMapping("/removeF")
	public String removeFavL(@RequestParam("adId") Long adId, Authentication authentication) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (!(auth instanceof AnonymousAuthenticationToken)) {
			favService.remove(adId, auth.getName().toString());
		}
		return "redirect:/Fav/list";
	}

	@GetMapping("/list")
	public String favList(ModelMap model, Authentication authentication) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (!(auth instanceof AnonymousAuthenticationToken)) {

			Optional<User> user = userRepository.findByUserName(auth.getName().toString());

			model.addAttribute("my_id", user.get().getId());
			model.addAttribute("userAds",
					AdPreviewConversor.fromAdListToAdPreviewList(favService.showAdsFavs(auth.getName().toString())));
			model.addAttribute("favs", favService.showFavs(auth.getName().toString()));
			model.addAttribute("following",
					FollowerConversor.fromFollowerToIdList(followService.findByFollowerPK_User(user.get())));
		}
		return "userList_page";
	}

}
