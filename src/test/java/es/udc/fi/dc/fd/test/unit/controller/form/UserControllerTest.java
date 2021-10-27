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

import es.udc.fi.dc.fd.controller.dto.RateDto;
import es.udc.fi.dc.fd.controller.entity.UserController;
import es.udc.fi.dc.fd.controller.util.FollowerConversor;
import es.udc.fi.dc.fd.model.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.fd.model.persistence.Ad;
import es.udc.fi.dc.fd.model.persistence.Follower;
import es.udc.fi.dc.fd.model.persistence.FollowerPK;
import es.udc.fi.dc.fd.model.persistence.User;
import es.udc.fi.dc.fd.repository.AdRepository;
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
public class UserControllerTest {

	private MockMvc mockMvc;

	private UserRepository userRepository;

	private FollowService followService;

	private UserService userService;

	private AdRepository adRepository;

	private FavService favService;

	private AdService adService;

	private List<Follower> followers = new ArrayList<Follower>(
			Arrays.asList(new Follower(new FollowerPK(Utils.createUser(), Utils.createUser()))));

	private List<Ad> ads = new ArrayList<Ad>(Arrays.asList(Utils.createAdWithAdUrl()));

	public UserControllerTest() {
		super();
	}

	@BeforeEach
	public final void setUpMockContext() throws InstanceNotFoundException {

		Utils.setAuthentication("user");

		mockMvc = MockMvcBuilders.standaloneSetup(getController()).build();
	}

	private final UserController getController() throws InstanceNotFoundException {

		userService = Mockito.mock(UserService.class);
		adRepository = Mockito.mock(AdRepository.class);
		favService = Mockito.mock(FavService.class);
		followService = Mockito.mock(FollowService.class);
		userRepository = Mockito.mock(UserRepository.class);
		adService = Mockito.mock(AdService.class);

		Mockito.when(userRepository.findByUserName(Mockito.any(String.class)))
				.thenReturn(Optional.of(Utils.createUser()));

		Mockito.when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(Utils.createUser()));

		Mockito.when(userService.getRating(Mockito.any(Long.class), Mockito.any(Long.class))).thenReturn(-1);

		Mockito.when(followService.findByFollowerPK_User(Mockito.any(User.class))).thenReturn(followers);

		Mockito.when(followService.removeFollow(Mockito.any(String.class), Mockito.any(String.class))).thenReturn(1L);

		Mockito.when(followService.follow(Mockito.any(String.class), Mockito.any(String.class)))
				.thenReturn(new Follower());

		Mockito.when(adRepository.findByUserIdIn(Mockito.anyList())).thenReturn(ads);

		Mockito.when(favService.showFavs(Mockito.any(String.class))).thenReturn(new ArrayList<Long>());

		Mockito.doNothing().when(userService).rateUser(Mockito.any(Long.class), Mockito.any(Long.class),
				Mockito.any(Integer.class));

		UserController userController = new UserController(userRepository, followService, userService, adRepository,
				favService, adService);

		return userController;
	}

	private final UserController getControllerProfileRated() throws InstanceNotFoundException {

		Mockito.when(userService.getRating(Mockito.any(Long.class), Mockito.any(Long.class))).thenReturn(1);

		UserController userController = new UserController(userRepository, followService, userService, adRepository,
				favService, adService);

		return userController;
	}

	@Test
	public final void showProfileTest() throws Exception {

		final ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/user/myProfile"));

		result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@Test
	public final void showUserProfileNoRatedTest() throws Exception {

		final ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/user/3"));

		result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
		result.andExpect(MockMvcResultMatchers.model().attribute("rated", false));
	}

	@Test
	public final void showUserProfileRatedTest() throws Exception {

		mockMvc = MockMvcBuilders.standaloneSetup(getControllerProfileRated()).build();

		final ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/user/3"));

		result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
		result.andExpect(MockMvcResultMatchers.model().attribute("rated", true));
	}

	@Test
	public final void showFollowingTest() throws Exception {

		final ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/user/following"));

		result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
		result.andExpect(MockMvcResultMatchers.model().attribute("following",
				FollowerConversor.fromFollowerToUserList(followers)));
	}

	@Test
	public final void showFollowingAdListTest() throws Exception {

		final ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/user/following/list"));

		result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@Test
	public final void unfollowTest() throws Exception {

		final ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/user/unfollow/1"));

		result.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@Test
	public final void followTest() throws Exception {

		final ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/user/follow/1"));

		result.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@Test
	public final void rateTest() throws Exception {

		final ResultActions result = mockMvc
				.perform(MockMvcRequestBuilders.post("/user/1/rate").flashAttr("rating", new RateDto(1)));

		result.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@Test
	public final void showRatingFormTest() throws Exception {

		final ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/user/1/rate"));

		result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
		result.andExpect(MockMvcResultMatchers.model().attribute("rated", false));
	}

	@Test
	public final void showRatingFormRatedBeforeTest() throws Exception {

		mockMvc = MockMvcBuilders.standaloneSetup(getControllerProfileRated()).build();

		final ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/user/1/rate"));

		result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
		result.andExpect(MockMvcResultMatchers.model().attribute("rated", true));
	}

}
