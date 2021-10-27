package es.udc.fi.dc.fd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.udc.fi.dc.fd.service.AdService;
import es.udc.fi.dc.fd.service.FavService;

@Controller
@RequestMapping("/list")
public class ListController {

	private static final String VIEW_LIST = "list";

	/**
	 * Default constructor.
	 */
	public ListController() {
		super();
	}

	@Autowired
	private AdService adService;

	@Autowired
	private FavService favService;

	/**
	 * Shows the welcome view.
	 * 
	 * @return the welcome view
	 */
	@GetMapping(produces = MediaType.TEXT_HTML_VALUE)
	public String showAdList(ModelMap model, Authentication authentication) {

		model.addAttribute("ads", adService.showAds());
		model.addAttribute("favs", favService.showFavs(authentication.getName().toString()));
		model.addAttribute("name", authentication.getName().toString());
		// }

		return VIEW_LIST;
	}
}
