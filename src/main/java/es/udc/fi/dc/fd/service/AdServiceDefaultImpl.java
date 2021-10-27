package es.udc.fi.dc.fd.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.model.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.fd.model.persistence.Ad;
import es.udc.fi.dc.fd.model.persistence.AdUrl;
import es.udc.fi.dc.fd.model.persistence.User;
import es.udc.fi.dc.fd.model.persistence.User.RoleType;
import es.udc.fi.dc.fd.repository.AdRepository;
import es.udc.fi.dc.fd.repository.AdSpecifications;
import es.udc.fi.dc.fd.repository.UserRepository;

@Service
@Transactional
public class AdServiceDefaultImpl implements AdService {

	@Autowired
	private AdRepository adRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public Ad createAd(Ad ad) {

		Optional<User> u = userRepository.findByUserName(ad.getUserName());

		if (u.get().getRole() == RoleType.PREMIUM) {
			ad.setPremium(true);
		} else {
			ad.setPremium(false);
		}

		return adRepository.save(ad);

	}

	private List<Ad> randomList(List<Ad> adList) {
		Map<Boolean, List<Ad>> grouped = adList.stream().collect(Collectors.partitioningBy(Ad::isPremium));
		List<Ad> premiumUsers = grouped.get(true);
		List<Ad> normalUsers = grouped.get(false);
		Collections.shuffle(premiumUsers);
		Collections.reverse(normalUsers);

		List<Ad> newAdList = Stream.concat(premiumUsers.stream(), normalUsers.stream()).collect(Collectors.toList());

		return newAdList;
	}

	@Override
	public Ad updateAdUrls(Ad ad, List<AdUrl> adUrls) {

		Ad newAd = adRepository.getOne(ad.getId());

		newAd.setAdUrls(new HashSet<AdUrl>(adUrls));

		return adRepository.save(newAd);

	}

	@Override
	@Transactional(readOnly = true)
	public List<Ad> showAds() {

		List<Ad> adList = adRepository.findAllByOrderByPremiumAsc();

		return randomList(adList);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Ad> showUserAds(String userName) {
		List<Ad> adList = adRepository.findByUserName(userName);

		return adList;
	}

	@Override
	@Transactional(readOnly = true)
	public Ad findAd(long id) throws InstanceNotFoundException {

		Optional<Ad> ad = adRepository.findById(id);

		if (!ad.isPresent()) {
			throw new InstanceNotFoundException("project.entities.ad", id);
		}

		return ad.get();
	}

	@Override
	public void holdAd(Long adId) {

		Ad ad = adRepository.findById(adId).get();

		ad.setHold(!ad.getHold());

		adRepository.updateAd(ad.getHold(), ad.getId());

	}

	@Override
	public List<Ad> customFindAds(String city, String keywords, LocalDate date_start, LocalDate date_end,
			BigDecimal price_min, BigDecimal price_max, float val_min) {

		List<Ad> adList = adRepository.findAll(AdSpecifications.findAdCustomSearch(city, keywords, date_start, date_end,
				price_min, price_max, val_min));

		return randomList(adList);

	}

	@Override
	public void removeAd(long id) throws InstanceNotFoundException {

		Ad ad = findAd(id);
		adRepository.delete(ad);
	}

	@Override
	public void buy(Long adId, Long buyerId) {

		Optional<Ad> ad = adRepository.findById(adId);

		LocalDate buyTime = LocalDate.now();

		ad.get().setSold(true);

		ad.get().setSoldDate(buyTime);

		ad.get().setBuyerId(buyerId);

	}

	@Override
	public List<Ad> showAdsPurchased(Long buyerId) {

		return adRepository.findByBuyerId(buyerId);
	}

}
