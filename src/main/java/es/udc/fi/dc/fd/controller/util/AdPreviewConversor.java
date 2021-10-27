package es.udc.fi.dc.fd.controller.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import es.udc.fi.dc.fd.controller.dto.AdPreviewDto;
import es.udc.fi.dc.fd.model.persistence.Ad;
import es.udc.fi.dc.fd.model.persistence.AdUrl;

public class AdPreviewConversor {

	public static AdPreviewDto fromAdToAdPreview(Ad ad) {

		List<Integer> images = new ArrayList<Integer>();

		int id = 1;
		for (AdUrl adUrl : ad.getAdUrls()) {

			images.add(id);
			id++;
		}

		if ((ad.getSoldDate() != null) && (ad.getSoldDate().isBefore(LocalDate.now()))) {
			return new AdPreviewDto(ad.getId(), ad.getAdName(), ad.getDescription(), ad.getUserName(), ad.getHold(),
					ad.getPrice(), ad.getCity(), ad.getDate(), ad.getUser().getId(), ad.isSold(), ad.getSoldDate(),
					true, new BigDecimal((ad.getUser().getRate() * 100) / 5), ad.getUser().getTimesRated(), images,
					ad.isPremium());
		} else {
			return new AdPreviewDto(ad.getId(), ad.getAdName(), ad.getDescription(), ad.getUserName(), ad.getHold(),
					ad.getPrice(), ad.getCity(), ad.getDate(), ad.getUser().getId(), ad.isSold(), ad.getSoldDate(),
					false, new BigDecimal((ad.getUser().getRate() * 100) / 5), ad.getUser().getTimesRated(), images,
					ad.isPremium());
		}

	}

	public static List<AdPreviewDto> fromAdListToAdPreviewList(List<Ad> adList) {

		List<AdPreviewDto> adPreviewDtos = new ArrayList<AdPreviewDto>();

		adList.forEach(ad -> adPreviewDtos.add(fromAdToAdPreview(ad)));

		return adPreviewDtos;
	}

}
