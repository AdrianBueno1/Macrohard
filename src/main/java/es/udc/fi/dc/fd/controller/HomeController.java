/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2020 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package es.udc.fi.dc.fd.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.udc.fi.dc.fd.controller.util.AdPreviewConversor;
import es.udc.fi.dc.fd.controller.util.FollowerConversor;
import es.udc.fi.dc.fd.model.persistence.User;
import es.udc.fi.dc.fd.repository.UserRepository;
import es.udc.fi.dc.fd.service.AdService;
import es.udc.fi.dc.fd.service.FavService;
import es.udc.fi.dc.fd.service.FollowService;

/**
 * Controller for home view.
 * 
 * @author Bernardo Mart&iacute;nez Garrido
 */
@Controller
@RequestMapping("/")
public class HomeController {

	/**
	 * Name for the welcome view.
	 */
	private static final String VIEW_WELCOME = "welcome";

	@Autowired
	private AdService adService;

	@Autowired
	private FavService favService;

	@Autowired
	private FollowService followService;

	@Autowired
	private UserRepository userRepository;

	/**
	 * Default constructor.
	 */
	public HomeController() {
		super();
	}

	@Autowired
	public HomeController(FollowService followService, AdService adService, FavService favService,
			UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
		this.followService = followService;
		this.adService = adService;
		this.favService = favService;
	}

	/**
	 * Shows the welcome view.
	 * 
	 * @return the welcome view
	 */
	@GetMapping(produces = MediaType.TEXT_HTML_VALUE)
	public String showWelcome(ModelMap model, Authentication authentication, HttpSession session) {

		if (authentication != null) {
			model.addAttribute("name", authentication.getName().toString());
			model.addAttribute("role", authentication.getAuthorities().toArray()[0]);

			User user = userRepository.findByUserName(authentication.getName().toString()).get();

			model.addAttribute("favs", favService.showFavs(authentication.getName().toString()));
			model.addAttribute("my_id", user.getId());
			model.addAttribute("following",
					FollowerConversor.fromFollowerToIdList(followService.findByFollowerPK_User(user)));
		}

		model.addAttribute("ads", AdPreviewConversor.fromAdListToAdPreviewList(adService.showAds()));

		return VIEW_WELCOME;
	}

}
