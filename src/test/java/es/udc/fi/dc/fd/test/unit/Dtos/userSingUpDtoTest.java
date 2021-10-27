package es.udc.fi.dc.fd.test.unit.Dtos;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.controller.dto.UserSignUpDto;

@RunWith(JUnitPlatform.class)
@SpringJUnitConfig
@Transactional
@Rollback
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:context/application-context.xml" })
@TestPropertySource({ "classpath:config/persistence-access.properties" })
public class userSingUpDtoTest {

	@Test
	public void adCoverageDtoTest() {

		UserSignUpDto user = new UserSignUpDto();
		user.setCity("city");
		user.setConfirmEmail("confirmEmail");
		user.setConfirmPassword("confirmPassword");
		user.setCreditCard("creditCard");
		user.setFirstName("firstName");
		user.setLastName("lastName");
		user.setUsername("username");
		user.setEmail("email");
		user.setPassword("password");

		assertNotNull(user.getCity());
		assertNotNull(user.getConfirmEmail());
		assertNotNull(user.getConfirmPassword());
		assertNotNull(user.getCreditCard());
		assertNotNull(user.getEmail());
		assertNotNull(user.getFirstName());
		assertNotNull(user.getLastName());
		assertNotNull(user.getPassword());
		assertNotNull(user.getUsername());
	}
}
