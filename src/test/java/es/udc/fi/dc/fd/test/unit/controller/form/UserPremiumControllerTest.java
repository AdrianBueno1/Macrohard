package es.udc.fi.dc.fd.test.unit.controller.form;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.controller.entity.UserPremiumController;
import es.udc.fi.dc.fd.model.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.fd.repository.UserRepository;
import es.udc.fi.dc.fd.service.UserService;
import es.udc.fi.dc.fd.test.config.Utils;

@RunWith(JUnitPlatform.class)
@SpringJUnitConfig
@Transactional
@Rollback
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:context/application-context.xml" })
@TestPropertySource({ "classpath:config/persistence-access.properties" })
public class UserPremiumControllerTest {

	private MockMvc mockMvc;

	private UserRepository userRepository;

	private UserService userService;

	public UserPremiumControllerTest() {
		super();
	}

	@BeforeEach
	public final void setUpMockContext() throws InstanceNotFoundException {

		Utils.setAuthentication("user");

		mockMvc = MockMvcBuilders.standaloneSetup(getController()).build();
	}

	private final UserPremiumController getController() throws InstanceNotFoundException {
		userRepository = Mockito.mock(UserRepository.class);
		userService = Mockito.mock(UserService.class);

		Mockito.when(userRepository.findByUserName(Mockito.any(String.class)))
				.thenReturn(Optional.of(Utils.createUser()));

		Mockito.when(userService.checkUser(Mockito.any(long.class))).thenReturn(Utils.createUser());

		UserPremiumController controller = new UserPremiumController(userService, userRepository);
		return controller;
	}

	@Test
	public final void getShowPremiumRequestTest() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/premium")).andExpect(MockMvcResultMatchers.status().isOk());

	}

	@Test
	public final void postFormRequest() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.post("/premium"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());

	}

}
