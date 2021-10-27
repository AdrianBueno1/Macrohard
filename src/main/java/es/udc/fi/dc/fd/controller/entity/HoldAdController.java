package es.udc.fi.dc.fd.controller.entity;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.udc.fi.dc.fd.service.AdService;

@Controller
@RequestMapping("/hold")
public class HoldAdController {

	@Autowired
	private AdService adService;

	
	public HoldAdController(AdService adService) {
		super();
		this.adService = adService;
	}




	@PostMapping
	public String holdAd(@RequestParam("adId") Long adId, HttpServletRequest request) {
		adService.holdAd(adId);
		return "redirect:" + request.getHeader("Referer");
	}

}
