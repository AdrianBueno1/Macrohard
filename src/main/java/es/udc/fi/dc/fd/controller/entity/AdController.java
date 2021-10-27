package es.udc.fi.dc.fd.controller.entity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.udc.fi.dc.fd.controller.dto.AdPreviewDto;
import es.udc.fi.dc.fd.controller.dto.AdSearchCustomDto;
import es.udc.fi.dc.fd.controller.util.AdPreviewConversor;
import es.udc.fi.dc.fd.controller.util.FollowerConversor;
import es.udc.fi.dc.fd.model.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.fd.model.persistence.Ad;
import es.udc.fi.dc.fd.model.persistence.AdUrl;
import es.udc.fi.dc.fd.model.persistence.User;
import es.udc.fi.dc.fd.repository.UserRepository;
import es.udc.fi.dc.fd.service.AdService;
import es.udc.fi.dc.fd.service.AdUrlService;
import es.udc.fi.dc.fd.service.FavService;
import es.udc.fi.dc.fd.service.FollowService;

@Controller
@RequestMapping("/ads")
public class AdController {

	private AdService adService;

	private AdUrlService adUrlService;

	private MessageSource messageSource;

	private FavService favService;

	private UserRepository userRepository;

	private FollowService followService;

	@ModelAttribute("search")
	public AdSearchCustomDto adSearchCustomDto() {
		return new AdSearchCustomDto();
	}

	@Autowired
	public AdController(AdService adService, MessageSource messageSource, FavService favService,
			UserRepository userRepository, FollowService followService, AdUrlService adUrlService) {
		super();

		this.adService = adService;
		this.messageSource = messageSource;
		this.favService = favService;
		this.userRepository = userRepository;
		this.followService = followService;
		this.adUrlService = adUrlService;
	}

	@GetMapping("/{id}")
	public String adDetails(@PathVariable("id") long id, ModelMap model, Authentication authentication) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		Ad ad = null;

		try {
			ad = adService.findAd(id);
		} catch (InstanceNotFoundException e) {
			return "404";
		}

		AdPreviewDto adPreviewDto = AdPreviewConversor.fromAdToAdPreview(ad);

		model.addAttribute("ad", adPreviewDto);

		if (!(auth instanceof AnonymousAuthenticationToken)) {
			model.addAttribute("name", auth.getName().toString());
			User user = userRepository.findByUserName(auth.getName().toString()).get();

			model.addAttribute("favs", favService.showFavs(auth.getName().toString()));
			model.addAttribute("my_id", user.getId());
			model.addAttribute("following",
					FollowerConversor.fromFollowerToIdList(followService.findByFollowerPK_User(user)));
		}

		return "ad_details";
	}

	@GetMapping("/search")
	public String adSearchCustom(@RequestParam Map<String, String> reqParam, ModelMap model,
			Authentication authentication, HttpSession session) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (!(auth instanceof AnonymousAuthenticationToken)) {
			User user = userRepository.findByUserName(auth.getName().toString()).get();

			model.addAttribute("favs", favService.showFavs(auth.getName().toString()));
			model.addAttribute("my_id", user.getId());
			model.addAttribute("timesRated", user.getTimesRated());
			model.addAttribute("following",
					FollowerConversor.fromFollowerToIdList(followService.findByFollowerPK_User(user)));
		}

		List<Ad> ads = null;

		String keywords = reqParam.get("keywords") == null || reqParam.get("keywords").equals("") ? null
				: reqParam.get("keywords");
		String city = reqParam.get("city") == null || reqParam.get("city").equals("") ? null : reqParam.get("city");
		LocalDate date_start = reqParam.get("date_start") == null ? null : LocalDate.parse(reqParam.get("date_start"));
		LocalDate date_end = reqParam.get("date_end") == null ? null : LocalDate.parse(reqParam.get("date_end"));
		BigDecimal price_min = reqParam.get("price_min") == null ? null : new BigDecimal(reqParam.get("price_min"));
		BigDecimal price_max = reqParam.get("price_max") == null ? null : new BigDecimal(reqParam.get("price_max"));
		int val_min = reqParam.get("val_min") == null ? 0 : Integer.parseInt(reqParam.get("val_min"));

		if (reqParam.get("keywords") != null) {

			ads = adService.customFindAds(city, keywords, date_start, date_end, price_min, price_max, val_min);

			model.addAttribute("ads", AdPreviewConversor.fromAdListToAdPreviewList(ads));
		}

		return "ad_search";
	}

	@PostMapping("/search")
	public String doAdSearchCustom(@ModelAttribute("search") @Valid AdSearchCustomDto adDto, final BindingResult errors,
			ModelMap model, final HttpServletResponse response, Locale locale, RedirectAttributes redirectAttributes) {

		if (errors.hasErrors()) {
			model.addAttribute("registerError", true);
			return "ad_search";
		}

		if (adDto.getDate_end() != null && adDto.getDate_start() != null) {
			if (adDto.getDate_end().isBefore(adDto.getDate_start())) {
				errors.rejectValue("date_end", "date_end_before_start",
						messageSource.getMessage("Search.date.end.before", null, locale));
				return "ad_search";
			}
		}

		if (adDto.getPrice_max() != null && adDto.getPrice_min() != null) {
			if (adDto.getPrice_max().compareTo(adDto.getPrice_min()) == -1) {
				errors.rejectValue("price_max", "price_max_less_than_min",
						messageSource.getMessage("Search.price.max.less", null, locale));
				return "ad_search";
			}
		}

		redirectAttributes.addAttribute("keywords", adDto.getKeywords());
		redirectAttributes.addAttribute("city", adDto.getCity());
		redirectAttributes.addAttribute("date_start",
				adDto.getDate_start() == null ? null : adDto.getDate_start().toString());
		redirectAttributes.addAttribute("date_end",
				adDto.getDate_end() == null ? null : adDto.getDate_end().toString());
		redirectAttributes.addAttribute("price_min", adDto.getPrice_min());
		redirectAttributes.addAttribute("price_max", adDto.getPrice_max());
		redirectAttributes.addAttribute("val_min", adDto.getVal_min() == 0 ? 0 : adDto.getVal_min());

		return "redirect:/ads/search";
	}

	@GetMapping("/{id}/images/{imageId}")
	public void getAdPreviewImage(@PathVariable("id") long id, @PathVariable("imageId") long imageId,
			HttpServletResponse response) {

		response.setContentType("image/jpeg");

		Ad ad = null;

		try {
			ad = adService.findAd(id);
		} catch (InstanceNotFoundException e) {
			e.printStackTrace();
		}

		AdUrl adurl = adUrlService.findAdImage(ad, imageId);

		InputStream inputStream = new ByteArrayInputStream(adurl.getData());

		try {
			IOUtils.copy(inputStream, response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
