package es.udc.fi.dc.fd.test.unit.service;

import java.math.BigDecimal;
import java.time.LocalDate;

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

import es.udc.fi.dc.fd.model.exceptions.DuplicateEmailException;
import es.udc.fi.dc.fd.model.exceptions.DuplicateUserException;
import es.udc.fi.dc.fd.model.exceptions.IncorrectCreditCardException;
import es.udc.fi.dc.fd.model.exceptions.IncorrectLoginException;
import es.udc.fi.dc.fd.model.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.fd.model.persistence.Ad;
import es.udc.fi.dc.fd.model.persistence.Rating;
import es.udc.fi.dc.fd.model.persistence.User;
import es.udc.fi.dc.fd.model.persistence.User.RoleType;
import es.udc.fi.dc.fd.repository.RatingRepository;
import es.udc.fi.dc.fd.repository.UserRepository;
import es.udc.fi.dc.fd.service.AdService;
import es.udc.fi.dc.fd.service.UserService;

@RunWith(JUnitPlatform.class)
@SpringJUnitConfig
@Transactional
@Rollback
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:context/application-context.xml" })
@TestPropertySource({ "classpath:config/persistence-access.properties" })
public class UserServiceDefaultImplTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private AdService adService;

	@Autowired
	private RatingRepository ratingRepository;

	private String invalidCreditCard = "123";

	private User createUser(String username) {
		return new User(username, username, username, username, username + "@gmail.com", "City Test",
				"1234567890123456", RoleType.USER, 5.0f, 3);
	}

	private Ad createAd(String adName, User user) {
		return new Ad(0L, adName, "Description test", null, user.getUserName(), user, false, new BigDecimal(10),
				"City Test", LocalDate.now(), false, false, null, 0L);
	}

	@Test
	public void signUpTest() throws DuplicateUserException, DuplicateEmailException, IncorrectCreditCardException {
		User u = createUser("username");
		userService.signUp(u);

		Assert.assertEquals(u, userRepository.findByUserName(u.getUserName()).get());
	}

	@Test
	public void signUpDuplicateUserTest()
			throws DuplicateUserException, DuplicateEmailException, IncorrectCreditCardException {
		User u = createUser("username");
		userService.signUp(u);

		Assertions.assertThrows(DuplicateUserException.class, () -> {
			userService.signUp(u);
		});
	}

	@Test
	public void signUpDuplicateEmailTest()
			throws DuplicateUserException, DuplicateEmailException, IncorrectCreditCardException {
		User u = createUser("username");
		userService.signUp(u);
		User u2 = new User("u", "u", "u", "u", "username@gmail.com", "city", RoleType.USER);
		Assertions.assertThrows(DuplicateEmailException.class, () -> {
			userService.signUp(u2);
		});
	}

	@Test
	public void signUpIncorrectCreditCardTest()
			throws DuplicateUserException, DuplicateEmailException, IncorrectCreditCardException {
		User u = createUser("username");
		u.setCreditCard(invalidCreditCard);

		Assertions.assertThrows(IncorrectCreditCardException.class, () -> {
			userService.signUp(u);
		});
	}

	@Test
	public void loginTest() throws IncorrectLoginException, DuplicateUserException, DuplicateEmailException,
			IncorrectCreditCardException {
		User u = createUser("username");
		userService.signUp(u);
		Assert.assertEquals(u, userService.login("username", "username"));
	}

	@Test
	public void incorrectLoginTest() throws IncorrectLoginException, DuplicateUserException, DuplicateEmailException,
			IncorrectCreditCardException {
		User u = createUser("username");
		userService.signUp(u);

		Assertions.assertThrows(IncorrectLoginException.class, () -> {
			userService.login("pepe", "username");
		});
	}

	@Test
	public void incorrectLoginPWTest() throws IncorrectLoginException, DuplicateUserException, DuplicateEmailException,
			IncorrectCreditCardException {
		User u = createUser("username");
		userService.signUp(u);

		Assertions.assertThrows(IncorrectLoginException.class, () -> {
			userService.login("username", "username2");
		});
	}

	@Test
	public void checkUserTest() throws IncorrectLoginException, DuplicateUserException, DuplicateEmailException,
			InstanceNotFoundException, IncorrectCreditCardException {
		User u = createUser("username");
		userService.signUp(u);
		Assert.assertEquals(u, userService.checkUser(userRepository.findByUserName(u.getUserName()).get().getId()));
	}

	@Test
	public void becomePremiumTest() throws IncorrectLoginException, DuplicateUserException, DuplicateEmailException,
			InstanceNotFoundException, IncorrectCreditCardException {
		User u = createUser("username");
		userService.signUp(u);

		userService.becomePremium(u.getUserName());

		Assert.assertEquals(RoleType.PREMIUM, u.getRole());
	}

	@Test
	public void becomeNotPremiumTest() throws IncorrectLoginException, DuplicateUserException, DuplicateEmailException,
			InstanceNotFoundException, IncorrectCreditCardException {
		User u = createUser("username");
		userService.signUp(u);
		u.setRole(RoleType.PREMIUM);

		userService.becomePremium(u.getUserName());

		Assert.assertEquals(RoleType.USER, u.getRole());
	}

	@Test
	public void becomePremiumAdsTest() throws IncorrectLoginException, DuplicateUserException, DuplicateEmailException,
			InstanceNotFoundException, IncorrectCreditCardException {
		User u = createUser("username");
		userService.signUp(u);

		Ad createAd = createAd("anuncio", u);
		Ad createAd1 = createAd("anuncio1", u);
		Ad createAd2 = createAd("anuncio2", u);

		Ad a = adService.createAd(createAd);
		Ad a1 = adService.createAd(createAd1);
		Ad a2 = adService.createAd(createAd2);

		Assert.assertEquals(false, a.isPremium());
		Assert.assertEquals(false, a1.isPremium());
		Assert.assertEquals(false, a2.isPremium());

		userService.becomePremium(u.getUserName());

		Ad findAd = adService.findAd(a.getId());
		Ad findAd1 = adService.findAd(a1.getId());
		Ad findAd2 = adService.findAd(a2.getId());

		Assert.assertEquals(RoleType.PREMIUM, u.getRole());

		Assert.assertTrue(findAd.isPremium());
		Assert.assertTrue(findAd1.isPremium());
		Assert.assertTrue(findAd2.isPremium());

		Ad a3 = createAd("anuncio3", u);
		Ad a4 = createAd("anuncio4", u);
		Ad a5 = createAd("anuncio5", u);

		Ad findAd3 = adService.createAd(a3);
		Ad findAd4 = adService.createAd(a4);
		Ad findAd5 = adService.createAd(a5);

		Assert.assertEquals(true, findAd3.isPremium());
		Assert.assertEquals(true, findAd4.isPremium());
		Assert.assertEquals(true, findAd5.isPremium());
	}

	@Test
	public void rateUserTest() throws IncorrectLoginException, DuplicateUserException, DuplicateEmailException,
			InstanceNotFoundException, IncorrectCreditCardException {

		User user = userRepository.save(createUser("TEST_USER"));

		User seller = userRepository.save(createUser("TEST_SELLER"));
		seller.setRate(3.5F);
		seller.setTimesRated(20);

		seller = userRepository.save(seller);

		ratingRepository.save(new Rating(0L, user.getId(), seller.getId(), 1));

		userService.rateUser(user.getId(), seller.getId(), 5);

		Assert.assertEquals((3.5F * 20 - 1 + 5) / 20, userRepository.findById(seller.getId()).get().getRate(), 0);
		Assert.assertEquals(20, userRepository.findById(seller.getId()).get().getTimesRated());
	}

	@Test
	public void rateUserNoRatedBeforeTest() throws IncorrectLoginException, DuplicateUserException,
			DuplicateEmailException, InstanceNotFoundException, IncorrectCreditCardException {

		User user = userRepository.save(createUser("TEST_USER"));

		User seller = userRepository.save(createUser("TEST_SELLER"));
		seller.setRate(3.5F);
		seller.setTimesRated(20);

		seller = userRepository.save(seller);
		userService.rateUser(user.getId(), seller.getId(), 5);

		Assert.assertEquals((3.5F * 20 + 5) / (20 + 1), userRepository.findById(seller.getId()).get().getRate(), 0);
		Assert.assertEquals((20 + 1), userRepository.findById(seller.getId()).get().getTimesRated());

	}

}
