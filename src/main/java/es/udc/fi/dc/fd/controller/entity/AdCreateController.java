package es.udc.fi.dc.fd.controller.entity;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import es.udc.fi.dc.fd.controller.dto.AdCreateDto;
import es.udc.fi.dc.fd.model.persistence.Ad;
import es.udc.fi.dc.fd.model.persistence.AdUrl;
import es.udc.fi.dc.fd.model.persistence.AdUrlPK;
import es.udc.fi.dc.fd.model.persistence.User;
import es.udc.fi.dc.fd.repository.UserRepository;
import es.udc.fi.dc.fd.service.AdService;
import es.udc.fi.dc.fd.service.AdUrlService;

@Controller
@RequestMapping("/createAd")
@PropertySource(value = { "classpath:config/persistence.properties" })
public class AdCreateController {

	private AdService adService;

	private AdUrlService adUrlService;

	private UserRepository userRepository;

	@Autowired
	public AdCreateController(AdService adService, AdUrlService adUrlService, UserRepository userRepository) {
		super();

		this.adService = adService;
		this.adUrlService = adUrlService;
		this.userRepository = userRepository;
	}

	@ModelAttribute("ad")
	public AdCreateDto adCreateDto() {
		return new AdCreateDto();
	}

	@GetMapping()
	public String showRegister(ModelMap model) {
		return "ad_form";
	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String addAd(@RequestPart(value = "images") List<MultipartFile> images,
			@ModelAttribute("ad") @Valid AdCreateDto adCreateDto, BindingResult result, ModelMap modelMap,
			Authentication authentication) throws IOException, ServletException {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		Ad ad = new Ad();
		List<MultipartFile> files = images;
		List<AdUrl> adUrls = new ArrayList<AdUrl>();

		ad.setDescription(adCreateDto.getDescription());
		ad.setAdName(adCreateDto.getAdName());
		ad.setCity(adCreateDto.getCity());
		ad.setPrice(adCreateDto.getPrice());
		ad.setDate(LocalDate.now());
		ad.setUserName(auth.getName());
		ad.setSold(false);

		User user = userRepository.findByUserName(auth.getName().toString()).get();
		ad.setPremium(adCreateDto.isPremium());
		ad.setUser(user);
		ad = adService.createAd(ad);

		long key = 1;
		if (null != files && files.size() > 0) {
			for (MultipartFile multipartFile : files) {

				adUrls.add(adUrlService.createAdUrl(new AdUrl(new AdUrlPK(ad, key), multipartFile.getOriginalFilename(),
						multipartFile.getBytes())));

				key++;
			}
		}

		adService.updateAdUrls(ad, adUrls);

		return "redirect:/ads/" + ad.getId();
	}

}
