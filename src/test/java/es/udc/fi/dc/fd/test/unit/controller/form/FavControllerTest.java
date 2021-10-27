package es.udc.fi.dc.fd.test.unit.controller.form;

import java.util.ArrayList;
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
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import es.udc.fi.dc.fd.controller.entity.FavController;
import es.udc.fi.dc.fd.model.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.fd.model.persistence.Ad;
import es.udc.fi.dc.fd.model.persistence.Follower;
import es.udc.fi.dc.fd.model.persistence.User;
import es.udc.fi.dc.fd.repository.UserRepository;
import es.udc.fi.dc.fd.service.FavService;
import es.udc.fi.dc.fd.service.FollowService;
import es.udc.fi.dc.fd.test.config.Utils;

@RunWith(JUnitPlatform.class)
@SpringJUnitConfig
@Transactional
@Rollback
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:context/application-context.xml" })
@TestPropertySource({ "classpath:config/persistence-access.properties" })
public class FavControllerTest {

	private MockMvc mockMvc;

	private UserRepository userRepository;

	private FollowService followService;

	private FavService favService;

	public FavControllerTest() {
		super();
	}

	@BeforeEach
	public final void setUpMockContext() throws InstanceNotFoundException {

		Utils.setAuthentication("user");

		mockMvc = MockMvcBuilders.standaloneSetup(getController()).build();
	}

	private final FavController getController() throws InstanceNotFoundException {

		favService = Mockito.mock(FavService.class);
		followService = Mockito.mock(FollowService.class);
		userRepository = Mockito.mock(UserRepository.class);

		Mockito.when(userRepository.findByUserName(Mockito.any(String.class)))
				.thenReturn(Optional.of(Utils.createUser()));

		Mockito.when(favService.showAdsFavs(Mockito.any(String.class))).thenReturn(new ArrayList<Ad>());

		Mockito.when(followService.findByFollowerPK_User(Mockito.any(User.class)))
				.thenReturn(new ArrayList<Follower>());

		FavController controller = new FavController(favService, userRepository, followService);

		return controller;
	}

	@Test
	public final void getFavViewRequestTest() throws Exception {

		final ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/Fav/list"));

		result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@Test
	public final void postFavRequestTest() throws Exception {

		final ResultActions result = mockMvc.perform(postFormRequest());

		result.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@Test
	public final void postRemoveTest() throws Exception {

		final ResultActions result = mockMvc.perform(postRemoveFormRequest());

		result.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@Test
	public final void postRemoveFTest() throws Exception {

		final ResultActions result = mockMvc.perform(postRemoveFFormRequest());

		result.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	private final RequestBuilder postFormRequest() {

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		Long p = 0L;
		params.add("adId", p.toString());

		return MockMvcRequestBuilders.post("/Fav").params(params);

	}

	private final RequestBuilder postRemoveFormRequest() {

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		Long p = 0L;
		params.add("adId", p.toString());

		return MockMvcRequestBuilders.post("/Fav/remove").params(params);

	}

	private final RequestBuilder postRemoveFFormRequest() {

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		Long p = 0L;
		params.add("adId", p.toString());

		return MockMvcRequestBuilders.post("/Fav/removeF").params(params);

	}

}
