package es.udc.fi.dc.fd.test.unit.controller.form;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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

import es.udc.fi.dc.fd.controller.dto.AdCreateDto;
import es.udc.fi.dc.fd.controller.entity.AdCreateController;
import es.udc.fi.dc.fd.model.persistence.Ad;
import es.udc.fi.dc.fd.model.persistence.AdUrl;
import es.udc.fi.dc.fd.model.persistence.User;
import es.udc.fi.dc.fd.model.persistence.User.RoleType;
import es.udc.fi.dc.fd.repository.UserRepository;
import es.udc.fi.dc.fd.service.AdService;
import es.udc.fi.dc.fd.service.AdUrlService;
import es.udc.fi.dc.fd.test.config.Utils;

@RunWith(JUnitPlatform.class)
@SpringJUnitConfig
@Transactional
@Rollback
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:context/application-context.xml" })
@TestPropertySource({ "classpath:config/persistence-access.properties" })
public class AdCreateControllerShowFormTest {

	private MockMvc mockMvc;

	private AdService adService;

	private AdUrlService adUrlService;

	private UserRepository userRepository;

	public AdCreateControllerShowFormTest() {
		super();
	}

	@BeforeEach
	public final void setUpMockContext() {

		mockMvc = MockMvcBuilders.standaloneSetup(getController()).build();
	}

	private final AdCreateController getController() {

		adService = Mockito.mock(AdService.class);
		adUrlService = Mockito.mock(AdUrlService.class);
		userRepository = Mockito.mock(UserRepository.class);

		User u = new User("user", "user", "user", "user", "user", "user", RoleType.USER);
		u.setId(1L);

		Mockito.when(userRepository.findByUserName(Mockito.any(String.class))).thenReturn(Optional.of(u));

		Mockito.when(adService.createAd(Mockito.any(Ad.class))).thenReturn(new Ad(1L, "test", "test", null, "user",

				Utils.createUser(), true, new BigDecimal(10), "city", LocalDate.now(), false, false, null, 0L));

		Mockito.when(adUrlService.createAdUrl(Mockito.any(AdUrl.class))).thenReturn(new AdUrl());

		Mockito.when(adService.updateAdUrls(Mockito.any(Ad.class), Mockito.anyList())).thenReturn(null);

		AdCreateController controller = new AdCreateController(adService, adUrlService, userRepository);

		return controller;
	}

	private final RequestBuilder getViewRequest() {
		return MockMvcRequestBuilders.get("/createAd");
	}

	private final RequestBuilder postFormRequest() throws IOException {
		MockMultipartFile file = new MockMultipartFile("images", "hello.txt", MediaType.MULTIPART_FORM_DATA_VALUE,
				"".getBytes());

		AdCreateDto ad = new AdCreateDto();
		ad.setAdName("ad name");
		ad.setCity("city");
		ad.setDescription("desc");
		ad.setPrice(new BigDecimal(10));
		ad.setPremium(true);

		return MockMvcRequestBuilders.multipart("/createAd").file(file).flashAttr("ad", ad);
	}

	@Test
	public final void testShowForm_ExpectedAttributeModel() throws Exception {

		final ResultActions result; // Request result

		result = mockMvc.perform(getViewRequest());

		// The response model contains the expected attributes
		result.andExpect(MockMvcResultMatchers.model().attributeExists("ad"));
	}

	@Test
	public final void testSendFormData_ExpectedView() throws Exception {

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);

		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);

		Mockito.when(authentication.getName()).thenReturn("user");

		final ResultActions result = mockMvc.perform(postFormRequest());

		result.andExpect(MockMvcResultMatchers.status().is3xxRedirection());

	}
}
