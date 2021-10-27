package es.udc.fi.dc.fd.test.unit.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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

import es.udc.fi.dc.fd.model.persistence.Ad;
import es.udc.fi.dc.fd.model.persistence.Fav;
import es.udc.fi.dc.fd.model.persistence.User;
import es.udc.fi.dc.fd.model.persistence.User.RoleType;
import es.udc.fi.dc.fd.repository.AdRepository;
import es.udc.fi.dc.fd.repository.FavRepository;
import es.udc.fi.dc.fd.repository.UserRepository;

@RunWith(JUnitPlatform.class)
@SpringJUnitConfig
@Transactional
@Rollback
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:context/application-context.xml" })
@TestPropertySource({ "classpath:config/persistence-access.properties" })
public class FavRepositoryTest {

	@Autowired
	private AdRepository adRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FavRepository favRepository;

	private User createUser(String username) {
		return new User(username, username, username, username, "email@gmail.com", "City Test", "1234567890123456",
				RoleType.USER);
	}

	private Ad createAd(String adName, User user) {

		return new Ad(1L, "user", "test", null, user.getUserName(), user, true, new BigDecimal(10), "city",
				LocalDate.now(), false, false, null, 0L);
	}

	private Fav createFav(Long adId, String userName) {
		return new Fav(0L, adId, userName);

	}

	@Test
	public void existsByAdIdTest() {

		User u = userRepository.save(createUser("username"));
		Ad ad = adRepository.save(createAd("Ad test name", u));
		favRepository.save(createFav(ad.getId(), u.getUserName()));

		Assert.assertTrue(favRepository.existsByAdId(ad.getId()));
	}

	@Test
	public void notExistsByAdIdTest() {

		User u = userRepository.save(createUser("username"));
		Ad ad = adRepository.save(createAd("Ad test name", u));
		favRepository.save(createFav(ad.getId(), u.getUserName()));

		Assert.assertFalse(favRepository.existsByAdId(0L));
	}

	@Test
	public void existsByUserNameTest() {

		User u = userRepository.save(createUser("username"));
		Ad ad = adRepository.save(createAd("Ad test name", u));
		favRepository.save(createFav(ad.getId(), u.getUserName()));

		Assert.assertTrue(favRepository.existsByUserName(u.getUserName()));
	}

	@Test
	public void notExistsByUserNameTest() {

		User u = userRepository.save(createUser("username"));
		Ad ad = adRepository.save(createAd("Ad test name", u));
		favRepository.save(createFav(ad.getId(), u.getUserName()));

		Assert.assertFalse(favRepository.existsByUserName(""));
	}

	@Test
	public void findByAdIdTest() {

		User u = userRepository.save(createUser("username"));
		Ad ad = adRepository.save(createAd("Ad test name", u));
		Fav fav = favRepository.save(createFav(ad.getId(), u.getUserName()));

		Assert.assertEquals(fav, favRepository.findByAdId(ad.getId()).get());
	}

	@Test
	public void notFindByAdIdTest() {

		Assertions.assertThrows(NoSuchElementException.class, () -> {
			favRepository.findByAdId(0L).get();
		});
	}

	@Test
	public void findByUserNameTest() {

		User u = userRepository.save(createUser("username"));
		Ad ad = adRepository.save(createAd("Ad test name", u));
		Fav fav = favRepository.save(createFav(ad.getId(), u.getUserName()));

		List<Fav> favs = new ArrayList<Fav>();
		favs.add(fav);

		Assert.assertEquals(new ArrayList<Fav>(), favRepository.findByUserName(""));
		Assert.assertEquals(favs, favRepository.findByUserName(u.getUserName()));
	}

	@Test
	public void deleteByUserNameandAdIdTest() {

		User u = userRepository.save(createUser("username"));
		Ad ad = adRepository.save(createAd("Ad test name", u));
		favRepository.save(createFav(ad.getId(), u.getUserName()));
		favRepository.deleteByAdIdAndUserName(ad.getId(), u.getUserName());

		Assert.assertFalse(favRepository.existsByAdId(ad.getId()));

	}

}
