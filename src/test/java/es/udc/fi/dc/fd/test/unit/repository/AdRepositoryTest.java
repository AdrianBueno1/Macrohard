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
import es.udc.fi.dc.fd.model.persistence.User;
import es.udc.fi.dc.fd.model.persistence.User.RoleType;
import es.udc.fi.dc.fd.repository.AdRepository;
import es.udc.fi.dc.fd.repository.UserRepository;

@RunWith(JUnitPlatform.class)
@SpringJUnitConfig
@Transactional
@Rollback
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:context/application-context.xml" })
@TestPropertySource({ "classpath:config/persistence-access.properties" })
public class AdRepositoryTest {

	@Autowired
	private AdRepository adRepository;

	@Autowired
	private UserRepository userRepository;

	private User createUser(String username) {
		return new User(username, username, username, username, "email@gmail.com", "City Test", "1234567890123456",
				RoleType.USER);
	}

	private Ad createAd(String adName, User user) {

		return new Ad(1L, "test", "test", null, user.getUserName(), user, true, new BigDecimal(10), "city",
				LocalDate.now(), false, false, null, 0L);
	}

	@Test
	public void existsByAdNameTest() {

		User u = userRepository.save(createUser("username"));
		Ad ad = adRepository.save(createAd("Ad test name", u));

		Assert.assertTrue(adRepository.existsByAdName(ad.getAdName()));
	}

	@Test
	public void notExistsByAdNameTest() {

		User u = userRepository.save(createUser("username"));
		adRepository.save(createAd("Ad test name", u));

		Assert.assertFalse(adRepository.existsByAdName(""));
	}

	@Test
	public void findByAdNameTest() {

		User u = userRepository.save(createUser("username"));
		Ad ad = adRepository.save(createAd("Ad test name", u));

		Assert.assertEquals(ad, adRepository.findByAdName(ad.getAdName()).get());
	}

	@Test
	public void notFindByAdNameTest() {

		Assertions.assertThrows(NoSuchElementException.class, () -> {
			adRepository.findByAdName("").get();
		});
	}

	@Test
	public void findByUserNameTest() {

		User u = userRepository.save(createUser("username"));
		Ad ad = adRepository.save(createAd("Ad test name", u));

		List<Ad> ads = new ArrayList<Ad>();
		ads.add(ad);

		Assert.assertEquals(new ArrayList<Ad>(), adRepository.findByUserName(""));
		Assert.assertEquals(ads, adRepository.findByUserName(u.getUserName()));
	}

	@Test
	public void findByUserIdInTest() {

		User u = userRepository.save(createUser("username"));
		Ad ad = adRepository.save(createAd("Ad test name", u));

		List<Long> users = new ArrayList<Long>();
		users.add(u.getId());

		List<Ad> ads = new ArrayList<Ad>();
		ads.add(ad);

		Assert.assertEquals(new ArrayList<Ad>(), adRepository.findByUserIdIn(new ArrayList<Long>()));
		Assert.assertEquals(ads, adRepository.findByUserIdIn(users));
	}
}
