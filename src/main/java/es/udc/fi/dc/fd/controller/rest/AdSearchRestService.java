package es.udc.fi.dc.fd.controller.rest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.udc.fi.dc.fd.controller.dto.AdPreviewDto;
import es.udc.fi.dc.fd.controller.util.AdPreviewConversor;
import es.udc.fi.dc.fd.model.persistence.Ad;
import es.udc.fi.dc.fd.service.AdService;

@RestController
@RequestMapping("api/search")
public class AdSearchRestService {

	private AdService adService;

	@Autowired
	public AdSearchRestService(AdService adService) {
		super();
		this.adService = adService;
	}

	@GetMapping()
	public List<AdPreviewDto> adSearchCustom(@RequestParam Map<String, String> reqParam) {

		List<Ad> ads = null;
		List<AdPreviewDto> adPreviewDtos = new ArrayList<AdPreviewDto>();

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

			adPreviewDtos = AdPreviewConversor.fromAdListToAdPreviewList(ads);
		}

		return adPreviewDtos;
	}

}
