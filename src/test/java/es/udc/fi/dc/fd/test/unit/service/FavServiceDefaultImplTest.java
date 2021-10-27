package es.udc.fi.dc.fd.test.unit.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.model.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.fd.model.persistence.Ad;
import es.udc.fi.dc.fd.model.persistence.Fav;
import es.udc.fi.dc.fd.model.persistence.User;
import es.udc.fi.dc.fd.model.persistence.User.RoleType;
import es.udc.fi.dc.fd.repository.AdRepository;
import es.udc.fi.dc.fd.repository.FavRepository;
import es.udc.fi.dc.fd.repository.UserRepository;
import es.udc.fi.dc.fd.service.AdService;
import es.udc.fi.dc.fd.service.FavService;

@RunWith(JUnitPlatform.class)
@SpringJUnitConfig
@Transactional
@Rollback
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:context/application-context.xml" })
@TestPropertySource({ "classpath:config/persistence-access.properties" })
public class FavServiceDefaultImplTest {

	@Autowired
	private AdService adService;

	@Autowired
	private FavService favService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AdRepository adRepository;

	@Autowired
	private FavRepository favRepository;

	private User createUser(String username) {
		return new User(username, username, username, username, username + "@gmail.com", "City Test",
				"1234567890123456", RoleType.USER);
	}

	private Ad createAd(String adName, User user) {

		return new Ad(0L, adName, "Description test", null, user.getUserName(), user, false, new BigDecimal(10),

				"City Test", LocalDate.now(), false, false, null, 0L);
	}

	private Fav createFav(Long adId, String userName) {
		return new Fav(0L, adId, userName);
	}

	@Test
	public void addFavTest() {

		User u = userRepository.save(createUser("username"));
		Ad ad = adRepository.save(createAd("Ad test name", u));

		Fav fav = favService.addFav(createFav(ad.getId(), u.getUserName()));

		Assert.assertEquals(fav, favRepository.findByAdId(ad.getId()).get());
	}

	@Test
	public void removeFavTest() {

		User u = userRepository.save(createUser("username"));
		Ad ad = adRepository.save(createAd("Ad test name", u));

		favService.addFav(createFav(ad.getId(), u.getUserName()));
		favService.remove(ad.getId(), u.getUserName());

		Assert.assertFalse(favRepository.existsByAdId(ad.getId()));
	}

	@Test
	public void showUserFavsTest() throws InstanceNotFoundException {

		User user = userRepository.save(createUser("username 1"));
		User user2 = userRepository.save(createUser("username 2"));

		Ad ad1 = adService.createAd(new Ad(0L, "Ad test name 1", "Description test", null, user.getUserName(), user,

				false, new BigDecimal(10), "A Coruña", LocalDate.now().minusDays(1), false, false, null, 0L));

		Ad ad2 = adService.createAd(new Ad(0L, "Ad test name 2", "Description test", null, user2.getUserName(), user2,
				false, new BigDecimal(10), "City Test", LocalDate.now(), false, false, null, 0L));

		Fav fav1 = favService.addFav(createFav(ad1.getId(), user.getUserName()));
		favService.addFav(createFav(ad2.getId(), user2.getUserName()));

		List<Long> favs = new ArrayList<Long>();

		favs.add(fav1.getAdId());

		Assert.assertEquals(favs, favService.showFavs(user.getUserName()));
	}

	@Test
	public void showUserFavsAdsTest() throws InstanceNotFoundException {

		User user = userRepository.save(createUser("username 1"));
		User user2 = userRepository.save(createUser("username 2"));

		Ad ad1 = adService.createAd(new Ad(0L, "Ad test name 1", "Description test", null, user.getUserName(), user,

				false, new BigDecimal(10), "A Coruña", LocalDate.now().minusDays(1), false, false, null, 0L));

		Ad ad2 = adService.createAd(new Ad(0L, "Ad test name 2", "Description test", null, user2.getUserName(), user2,
				false, new BigDecimal(10), "City Test", LocalDate.now(), false, false, null, 0L));

		favService.addFav(createFav(ad1.getId(), user.getUserName()));
		favService.addFav(createFav(ad2.getId(), user2.getUserName()));

		List<Ad> ads = new ArrayList<Ad>();

		ads.add(ad1);

		Assert.assertEquals(ads, favService.showAdsFavs(user.getUserName()));
	}

}
