package es.udc.fi.dc.fd.test.unit.controller.form;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.controller.dto.UserSignUpDto;
import es.udc.fi.dc.fd.controller.entity.UserSignUpController;
import es.udc.fi.dc.fd.model.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.fd.service.UserService;
import es.udc.fi.dc.fd.test.config.Utils;

@RunWith(JUnitPlatform.class)
@SpringJUnitConfig
@Transactional
@Rollback
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:context/application-context.xml" })
@TestPropertySource({ "classpath:config/persistence-access.properties" })
public class UserSignUpControllerTest {

	private MockMvc mockMvc;

	private UserService userService;

	private MessageSource messageSource;

	@BeforeEach
	public final void setUpMockContext() throws InstanceNotFoundException {

		Utils.setAuthentication("user");

		mockMvc = MockMvcBuilders.standaloneSetup(getController()).build();
	}

	private final UserSignUpController getController() throws InstanceNotFoundException {

		userService = Mockito.mock(UserService.class);
		messageSource = Mockito.mock(MessageSource.class);

		Mockito.when(userService.checkUser(Mockito.any(long.class))).thenReturn(Utils.createUser());

		UserSignUpController controller = new UserSignUpController(userService, messageSource);

		return controller;

	}

	@Test
	public final void getSignUpRequestTest() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/signUp")).andExpect(MockMvcResultMatchers.status().isOk());

	}

	@Test
	public final void signUpTest() throws Exception {

		final ResultActions result = mockMvc.perform(postFormRequest());

		result.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@Test
	public final void signUpIncorrectPasswordTest() throws Exception {

		final ResultActions result = mockMvc.perform(postFormIncorrectPasswordError());

		result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@Test
	public final void signUpIncorrectEmailTest() throws Exception {

		final ResultActions result = mockMvc.perform(postFormIncorrectEmailError());

		result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@Test
	public final void signUpDuplicateEmailTest() throws Exception {

		final ResultActions result = mockMvc.perform(postFormDuplicateEmailError());

		result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	private final RequestBuilder postFormRequest() throws IOException {

		UserSignUpDto user = new UserSignUpDto();

		user.setUsername("username");
		user.setFirstName("username");
		user.setLastName("username");
		user.setCity("city");
		user.setCreditCard("1234567890123456");
		user.setPassword("username");
		user.setConfirmPassword("username");
		user.setEmail("username@gmail.com");
		user.setConfirmEmail("username@gmail.com");

		return MockMvcRequestBuilders.multipart("/signUp").flashAttr("user", user);
	}

	private final RequestBuilder postFormIncorrectPasswordError() throws IOException {

		UserSignUpDto user = new UserSignUpDto();

		user.setUsername("username");
		user.setFirstName("username");
		user.setLastName("username");
		user.setCity("city");
		user.setCreditCard("1234567890123456");
		user.setPassword("username");
		user.setConfirmPassword("Notusername");
		user.setEmail("username@gmail.com");
		user.setConfirmEmail("username@gmail.com");

		return MockMvcRequestBuilders.multipart("/signUp").flashAttr("user", user);
	}

	private final RequestBuilder postFormIncorrectEmailError() throws IOException {

		UserSignUpDto user = new UserSignUpDto();

		user.setUsername("username");
		user.setFirstName("username");
		user.setLastName("username");
		user.setCity("city");
		user.setCreditCard("1234567890123456");
		user.setPassword("username");
		user.setConfirmPassword("username");
		user.setEmail("username@gmail.com");
		user.setConfirmEmail("Notusername@gmail.com");

		return MockMvcRequestBuilders.multipart("/signUp").flashAttr("user", user);
	}

	private final RequestBuilder postFormDuplicateEmailError() throws IOException {

		UserSignUpDto user = new UserSignUpDto();

		user.setUsername("username");
		user.setFirstName("username");
		user.setLastName("username");
		user.setCity("city");
		user.setCreditCard("1234567890123456");
		user.setPassword("username");
		user.setConfirmPassword("username");
		user.setEmail("username@gmail.com");
		user.setConfirmEmail("Notusername@gmail.com");

		UserSignUpDto user2 = new UserSignUpDto();

		user.setUsername("username2");
		user.setFirstName("username2");
		user.setLastName("username2");
		user.setCity("city2");
		user.setCreditCard("1234567890123456");
		user.setPassword("username2");
		user.setConfirmPassword("username2");
		user.setEmail("username@gmail.com");
		user.setConfirmEmail("Notusername@gmail.com");

		return MockMvcRequestBuilders.multipart("/signUp").flashAttr("user", user2);
	}

}
