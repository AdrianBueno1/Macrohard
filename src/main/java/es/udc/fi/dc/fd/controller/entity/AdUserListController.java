package es.udc.fi.dc.fd.controller.entity;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import es.udc.fi.dc.fd.model.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.fd.model.persistence.User;
import es.udc.fi.dc.fd.repository.UserRepository;
import es.udc.fi.dc.fd.service.AdService;
import es.udc.fi.dc.fd.service.FavService;
import es.udc.fi.dc.fd.service.FollowService;

@Controller
@RequestMapping("/userList")
public class AdUserListController {

	private AdService adService;

	private FavService favService;

	private UserRepository userRepository;

	private FollowService followService;

	@Autowired
	public AdUserListController(AdService adService, FavService favService, UserRepository userRepository,
			FollowService followService) {
		super();
		this.adService = adService;
		this.favService = favService;
		this.userRepository = userRepository;
		this.followService = followService;
	}

	@PostMapping("/removeA")
	public String removeFavL(@RequestParam("adId") Long adId, Authentication authentication, HttpServletRequest request)
			throws InstanceNotFoundException {

		adService.removeAd(adId);

		return "redirect:" + request.getHeader("Referer");
	}

	@GetMapping(produces = MediaType.TEXT_HTML_VALUE)
	public String showUserAdList(ModelMap model, Authentication authentication) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		model.addAttribute("userAds",
				AdPreviewConversor.fromAdListToAdPreviewList(adService.showUserAds(auth.getName())));
		model.addAttribute("favs", favService.showFavs(auth.getName().toString()));
		model.addAttribute("name", auth.getName().toString());

		User user = userRepository.findByUserName(auth.getName().toString()).get();

		model.addAttribute("my_id", user.getId());
		model.addAttribute("following",
				FollowerConversor.fromFollowerToIdList(followService.findByFollowerPK_User(user)));

		return "userList_page";
	}

}
