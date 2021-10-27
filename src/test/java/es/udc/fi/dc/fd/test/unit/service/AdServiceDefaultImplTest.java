package es.udc.fi.dc.fd.test.unit.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
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
import es.udc.fi.dc.fd.model.persistence.AdUrl;
import es.udc.fi.dc.fd.model.persistence.AdUrlPK;
import es.udc.fi.dc.fd.model.persistence.User;
import es.udc.fi.dc.fd.model.persistence.User.RoleType;
import es.udc.fi.dc.fd.repository.AdRepository;
import es.udc.fi.dc.fd.repository.UserRepository;
import es.udc.fi.dc.fd.service.AdService;
import es.udc.fi.dc.fd.service.AdUrlService;

@RunWith(JUnitPlatform.class)
@SpringJUnitConfig
@Transactional
@Rollback
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:context/application-context.xml" })
@TestPropertySource({ "classpath:config/persistence-access.properties" })
public class AdServiceDefaultImplTest {

	@Autowired
	private AdService adService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AdRepository adRepository;

	@Autowired
	private AdUrlService adUrlService;

	private static final int NUM_AD_URLS = 10;

	private User createUser(String username) {
		return new User(username, username, username, username, username + "@gmail.com", "City Test",
				"1234567890123456", RoleType.USER);
	}

	private Ad createAd(String adName, User user) {

		return new Ad(0L, adName, "Description test", null, user.getUserName(), user, false, new BigDecimal(10),

				"City Test", LocalDate.now(), false, false, null, 0L);
	}

	@Test
	public void createAdTest() {

		User u = userRepository.save(createUser("username"));
		Ad ad = adService.createAd(createAd("Ad test name", u));

		Assert.assertEquals(ad, adRepository.findById(ad.getId()).get());
	}

	@Test
	public void updateAdUrlsTest() {

		User u = userRepository.save(createUser("username"));
		Ad ad = adService.createAd(createAd("Ad test name", u));

		List<AdUrl> adUrls = new ArrayList<AdUrl>();
		for (int i = 0; i < NUM_AD_URLS; i++) {
			adUrls.add(adUrlService
					.createAdUrl(new AdUrl(new AdUrlPK(ad, (long) i + 1), "path img " + i, "test".getBytes())));
		}

		ad.setAdUrls(new HashSet<AdUrl>(adUrls));

		Ad expected = adService.updateAdUrls(ad, adUrls);

		Assert.assertEquals(ad, expected);
	}

	@Test
	public void findAdTest() throws InstanceNotFoundException {

		User u = userRepository.save(createUser("username"));
		Ad ad = adService.createAd(createAd("Ad test name", u));

		Assert.assertEquals(ad, adService.findAd(ad.getId()));
	}

	@Test
	public void notFindAdTest() throws InstanceNotFoundException {

		Assertions.assertThrows(InstanceNotFoundException.class, () -> {
			adService.findAd(-1);
		});
	}

	@Test
	public void holdAdFalseTest() throws InstanceNotFoundException {

		User user = userRepository.save(createUser("username"));

		Ad ad = adService.createAd(new Ad(0L, "Ad test name 1", "Description test", null, user.getUserName(), user,

				true, new BigDecimal(10), "A Coruña", LocalDate.now().plusDays(1), false, false, null, 0L));

		adService.holdAd(ad.getId());

		Assert.assertEquals(ad, adService.findAd(ad.getId()));
	}

	@Test
	public void holdAdTrueTest() throws InstanceNotFoundException {

		User u = userRepository.save(createUser("username"));
		Ad ad = adService.createAd(createAd("Ad test name", u));

		adService.holdAd(ad.getId());

		Assert.assertEquals(ad, adService.findAd(ad.getId()));
	}

	@Test
	public void customFindAdsTest() throws InstanceNotFoundException {

		User user = createUser("username");
		user.setRate(0F);
		user = userRepository.save(user);

		Ad ad1 = adService.createAd(new Ad(0L, "Ad test name 1", "Description test", null, user.getUserName(), user,

				false, new BigDecimal(10), "A Coruña", LocalDate.now().plusDays(1), false, false, null, 0L));

		adService.createAd(new Ad(0L, "Ad test name 2", "Description test", null, user.getUserName(), user, false,
				new BigDecimal(10), "City Test", LocalDate.now(), false, false, null, 0L));

		adService.createAd(new Ad(0L, "Ad test name 11", "Description test", null, user.getUserName(), user, false,
				new BigDecimal(10), "Santiago", LocalDate.now(), false, false, null, 0L));

		adService.createAd(new Ad(0L, "Ad test name 4", "Description test", null, user.getUserName(), user, false,
				new BigDecimal(10), "City Test", LocalDate.now(), false, false, null, 0L));

		Ad ad5 = adService.createAd(new Ad(0L, "Ad test name 111", "Description test", null, user.getUserName(), user,
				false, new BigDecimal(10), "Coruña", LocalDate.now(), false, false, null, 0L));

		Ad ad6 = adService.createAd(new Ad(0L, "Ad test name 1111", "Description test", null, user.getUserName(), user,
				false, new BigDecimal(12), "A Coruña", LocalDate.now().minusDays(1), false, false, null, 0L));

		List<Ad> ads = new ArrayList<Ad>();
		ads.add(ad6);
		ads.add(ad5);
		ads.add(ad1);

		Assert.assertEquals(ads, adService.customFindAds("Coruña", "name test 1", LocalDate.now().minusDays(1),
				LocalDate.now().plusDays(1), new BigDecimal(10), new BigDecimal(12), 0));

	}

	@Test
	public void showAdsTest() throws InstanceNotFoundException {

		User user = userRepository.save(createUser("username"));

		adService.createAd(new Ad(0L, "Ad test name 1", "Description test", null, user.getUserName(), user,

				false, new BigDecimal(10), "A Coruña", LocalDate.now().minusDays(1), false, false, null, 0L));

		adService.createAd(new Ad(0L, "Ad test name 2", "Description test", null, user.getUserName(), user, false,
				new BigDecimal(10), "City Test", LocalDate.now(), false, false, null, 0L));

		List<Ad> ads = new ArrayList<Ad>();
		ads = adRepository.findAll();
		Collections.reverse(ads);
		Assert.assertEquals(ads, adService.showAds());
	}

	@Test
	public void showUserAdsTest() throws InstanceNotFoundException {

		User user = userRepository.save(createUser("username 1"));
		User user2 = userRepository.save(createUser("username 2"));

		Ad ad1 = adService.createAd(new Ad(0L, "Ad test name 1", "Description test", null, user.getUserName(), user,

				false, new BigDecimal(10), "A Coruña", LocalDate.now().minusDays(1), false, false, null, 0L));

		adService.createAd(new Ad(0L, "Ad test name 2", "Description test", null, user2.getUserName(), user2, false,
				new BigDecimal(10), "City Test", LocalDate.now(), false, false, null, 0L));

		List<Ad> ads = new ArrayList<Ad>();

		ads.add(ad1);

		Assert.assertEquals(ads, adService.showUserAds(user.getUserName()));
	}

	@Test
	public void removeAdTest() throws InstanceNotFoundException {

		User user = userRepository.save(createUser("username"));

		Ad ad = adService.createAd(new Ad(0L, "Ad test name 1", "Description test", null, user.getUserName(), user,

				false, new BigDecimal(10), "A Coruña", LocalDate.now().minusDays(1), false, false, null, 0L));

		adService.removeAd(ad.getId());

		Assertions.assertThrows(InstanceNotFoundException.class, () -> {
			adService.findAd(ad.getId());
		});
	}

}
