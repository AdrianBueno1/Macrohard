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

import es.udc.fi.dc.fd.controller.entity.AdUserListController;
import es.udc.fi.dc.fd.model.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.fd.model.persistence.Ad;
import es.udc.fi.dc.fd.model.persistence.Follower;
import es.udc.fi.dc.fd.model.persistence.User;
import es.udc.fi.dc.fd.repository.UserRepository;
import es.udc.fi.dc.fd.service.AdService;
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
public class AdUserListControllerTest {

	private MockMvc mockMvc;

	private AdService adService;

	private FavService favService;

	private UserRepository userRepository;

	private FollowService followService;

	private List<Ad> ads = new ArrayList<Ad>(Arrays.asList(Utils.createAdWithAdUrl()));

	public AdUserListControllerTest() {
		super();
	}

	@BeforeEach
	public final void setUpMockContext() throws InstanceNotFoundException {

		Utils.setAuthentication("user");

		mockMvc = MockMvcBuilders.standaloneSetup(getController()).build();
	}

	private final AdUserListController getController() throws InstanceNotFoundException {

		adService = Mockito.mock(AdService.class);
		favService = Mockito.mock(FavService.class);
		followService = Mockito.mock(FollowService.class);
		userRepository = Mockito.mock(UserRepository.class);

		Mockito.when(userRepository.findByUserName(Mockito.any(String.class)))
				.thenReturn(Optional.of(Utils.createUser()));

		Mockito.when(adService.findAd(Mockito.any(long.class))).thenReturn(Utils.createAdWithAdUrl());

		Mockito.when(adService.showUserAds(Mockito.any(String.class))).thenReturn(ads);

		Mockito.when(favService.showFavs(Mockito.any(String.class))).thenReturn(new ArrayList<Long>());

		Mockito.when(followService.findByFollowerPK_User(Mockito.any(User.class)))
				.thenReturn(new ArrayList<Follower>());

		Mockito.doNothing().when(adService).removeAd(Mockito.any(Long.class));

		AdUserListController controller = new AdUserListController(adService, favService, userRepository,
				followService);

		return controller;
	}

	@Test
	public final void getAdViewRequestTest() throws Exception {

		final ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/userList"));

		result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@Test
	public final void followTest() throws Exception {

		final ResultActions result = mockMvc
				.perform(MockMvcRequestBuilders.post("/userList/removeA").param("adId", "1"));

		result.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

}
