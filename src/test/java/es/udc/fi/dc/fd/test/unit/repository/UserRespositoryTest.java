package es.udc.fi.dc.fd.test.unit.repository;

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

import es.udc.fi.dc.fd.model.persistence.User;
import es.udc.fi.dc.fd.model.persistence.User.RoleType;
import es.udc.fi.dc.fd.repository.UserRepository;

@RunWith(JUnitPlatform.class)
@SpringJUnitConfig
@Transactional
@Rollback
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:context/application-context.xml" })
@TestPropertySource({ "classpath:config/persistence-access.properties" })
public class UserRespositoryTest {

	@Autowired
	private UserRepository userRepository;

	private User createUser(String username) {
		return new User(username, username, username, username, username + "@gmail.com", "City Test",
				"1234567890123456", RoleType.USER);
	}

	@Test
	public void existsByUserNameTest() {

		User u = userRepository.save(createUser("username"));

		Assert.assertTrue(userRepository.existsByUserName(u.getUserName()));
	}

	@Test
	public void notExistsByUserNameTest() {

		userRepository.save(createUser("username"));

		Assert.assertFalse(userRepository.existsByUserName(""));
	}

	@Test
	public void existsByEmailTest() {

		User u = userRepository.save(createUser("username"));

		Assert.assertTrue(userRepository.existsByEmail(u.getEmail()));
	}

	@Test
	public void notExistsByEmailTest() {

		userRepository.save(createUser("username"));

		Assert.assertFalse(userRepository.existsByEmail(""));
	}

	@Test
	public void findByUserNameTest() {

		User u = userRepository.save(createUser("username"));

		Assert.assertEquals(u, userRepository.findByUserName(u.getUserName()).get());
	}

	@Test
	public void notFindByAdNameTest() {

		Assertions.assertThrows(NoSuchElementException.class, () -> {
			userRepository.findByUserName("").get();
		});
	}

	@Test
	public void findByIdTest() {

		User u = userRepository.save(createUser("username"));

		Assert.assertEquals(u, userRepository.findById(u.getId()).get());
	}

	@Test
	public void notFindByIdTest() {

		Assertions.assertThrows(NoSuchElementException.class, () -> {
			userRepository.findById(200L).get();
		});
	}

}
