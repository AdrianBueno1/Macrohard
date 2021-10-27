package es.udc.fi.dc.fd.test.unit.service;

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

import es.udc.fi.dc.fd.model.exceptions.DuplicateEmailException;
import es.udc.fi.dc.fd.model.exceptions.DuplicateUserException;
import es.udc.fi.dc.fd.model.exceptions.IncorrectCreditCardException;
import es.udc.fi.dc.fd.model.persistence.Follower;
import es.udc.fi.dc.fd.model.persistence.User;
import es.udc.fi.dc.fd.model.persistence.User.RoleType;
import es.udc.fi.dc.fd.service.FollowService;
import es.udc.fi.dc.fd.service.UserService;

@RunWith(JUnitPlatform.class)
@SpringJUnitConfig
@Transactional
@Rollback
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:context/application-context.xml" })
@TestPropertySource({ "classpath:config/persistence-access.properties" })
public class FollowServiceDefaultImplTest {

	@Autowired
	private FollowService followService;

	@Autowired
	private UserService userService;

	private User createUser(String username) {
		return new User(username, username, username, username, username + "@gmail.com", "City Test",
				"1234567890123456", RoleType.USER);
	}

	@Test
	public void followAndFindByFollowerPK_UserTest()
			throws DuplicateUserException, DuplicateEmailException, IncorrectCreditCardException {
		User u = createUser("username");
		User u2 = createUser("username2");
		User u3 = createUser("username3");
		User u4 = createUser("username4");

		userService.signUp(u);
		userService.signUp(u2);
		userService.signUp(u3);
		userService.signUp(u4);

		Follower follower1 = followService.follow("username", "username2");
		Follower follower2 = followService.follow("username", "username3");
		Follower follower3 = followService.follow("username", "username4");

		List<Follower> listFollower = new ArrayList<Follower>();
		listFollower.add(follower1);
		listFollower.add(follower2);
		listFollower.add(follower3);

		Assert.assertEquals(listFollower.size(), followService.findByFollowerPK_User(u).size());
		Assert.assertEquals(listFollower.get(0), followService.findByFollowerPK_User(u).get(0));

	}

	@Test
	public void removeFollowTest()
			throws DuplicateUserException, DuplicateEmailException, IncorrectCreditCardException {
		User u = createUser("username");
		User u2 = createUser("username2");
		User u3 = createUser("username3");
		User u4 = createUser("username4");

		userService.signUp(u);
		userService.signUp(u2);
		userService.signUp(u3);
		userService.signUp(u4);

		followService.follow("username", "username2");
		Follower follower2 = followService.follow("username", "username3");
		followService.follow("username", "username4");

		List<Follower> listFollower = followService.findByFollowerPK_User(u);

		Assert.assertEquals(3, listFollower.size());

		followService.removeFollow("username", "username2");

		listFollower = followService.findByFollowerPK_User(u);

		Assert.assertEquals(2, listFollower.size());

		Assert.assertEquals(follower2, listFollower.get(0));

	}

}
