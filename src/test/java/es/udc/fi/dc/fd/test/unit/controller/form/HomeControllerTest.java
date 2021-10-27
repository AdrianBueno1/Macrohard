package es.udc.fi.dc.fd.test.unit.controller.form;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.controller.HomeController;
import es.udc.fi.dc.fd.model.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.fd.model.persistence.Ad;
import es.udc.fi.dc.fd.model.persistence.Follower;
import es.udc.fi.dc.fd.model.persistence.FollowerPK;
import es.udc.fi.dc.fd.model.persistence.User;
import es.udc.fi.dc.fd.repository.UserRepository;
import es.udc.fi.dc.fd.service.AdService;
import es.udc.fi.dc.fd.service.FavService;
import es.udc.fi.dc.fd.service.FollowService;
import es.udc.fi.dc.fd.service.UserService;
import es.udc.fi.dc.fd.test.config.Utils;

@RunWith(JUnitPlatform.class)
@SpringJUnitConfig
@Transactional
@Rollback
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:context/application-context.xml" })
@TestPropertySource({ "classpath:config/persistence-access.properties" })
public class HomeControllerTest {

	private MockMvc mockMvc;

	private UserRepository userRepository;

	private FollowService followService;

	private UserService userService;

	private AdService adService;

	private FavService favService;

	private List<Follower> followers = new ArrayList<Follower>(
			Arrays.asList(new Follower(new FollowerPK(Utils.createUser(), Utils.createUser()))));

	public HomeControllerTest() {
		super();
	}

	@BeforeEach
	public final void setUpMockContext() throws InstanceNotFoundException {

		Utils.setAuthentication("user");

		mockMvc = MockMvcBuilders.standaloneSetup(getController()).build();
	}

	private final HomeController getController() throws InstanceNotFoundException {

		userService = Mockito.mock(UserService.class);
		adService = Mockito.mock(AdService.class);
		favService = Mockito.mock(FavService.class);
		followService = Mockito.mock(FollowService.class);
		userRepository = Mockito.mock(UserRepository.class);

		Mockito.when(userRepository.findByUserName(Mockito.any(String.class)))
				.thenReturn(Optional.of(Utils.createUser()));

		Mockito.when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(Utils.createUser()));

		Mockito.when(userService.getRating(Mockito.any(Long.class), Mockito.any(Long.class))).thenReturn(-1);

		Mockito.when(followService.findByFollowerPK_User(Mockito.any(User.class))).thenReturn(followers);

		Mockito.when(followService.removeFollow(Mockito.any(String.class), Mockito.any(String.class))).thenReturn(1L);

		Mockito.when(followService.follow(Mockito.any(String.class), Mockito.any(String.class)))
				.thenReturn(new Follower());

		Mockito.when(adService.updateAdUrls(Mockito.any(Ad.class), Mockito.anyList())).thenReturn(null);
		Mockito.when(adService.showAds()).thenReturn(new ArrayList<Ad>());

		Mockito.when(favService.showFavs(Mockito.any(String.class))).thenReturn(new ArrayList<Long>());

		HomeController homeController = new HomeController(followService, adService, favService, userRepository);

		return homeController;
	}

	@Test
	public final void showProfileTest() throws Exception {

		final ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/"));

		result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}
}
