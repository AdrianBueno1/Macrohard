package es.udc.fi.dc.fd.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import es.udc.fi.dc.fd.model.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.fd.model.persistence.Ad;
import es.udc.fi.dc.fd.model.persistence.AdUrl;

public interface AdService {
	/**
	 * Creates a new ad.
	 * 
	 * @param ad ad to create
	 */
	Ad createAd(Ad ad);

	List<Ad> showAds();

	Ad findAd(long id) throws InstanceNotFoundException;

	Ad updateAdUrls(Ad ad, List<AdUrl> adUrls);

	List<Ad> showUserAds(String userName);

	List<Ad> customFindAds(String city, String keywords, LocalDate date_start, LocalDate date_end, BigDecimal price_min,
			BigDecimal price_max, float val_min);

	void removeAd(long id) throws InstanceNotFoundException;

	void holdAd(Long adId);
	
	void buy(Long adId, Long buyerId);	

	List<Ad> showAdsPurchased (Long buyerId);
	
}
