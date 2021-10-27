package es.udc.fi.dc.fd.test.unit.controller.form;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

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

import es.udc.fi.dc.fd.controller.dto.AdSearchCustomDto;
import es.udc.fi.dc.fd.controller.entity.AdController;
import es.udc.fi.dc.fd.model.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.fd.model.persistence.Ad;
import es.udc.fi.dc.fd.model.persistence.Follower;
import es.udc.fi.dc.fd.model.persistence.User;
import es.udc.fi.dc.fd.repository.UserRepository;
import es.udc.fi.dc.fd.service.AdService;
import es.udc.fi.dc.fd.service.AdUrlService;
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
public class AdControllerTest {

	private MockMvc mockMvc;

	private AdService adService;

	private AdUrlService adUrlService;

	private UserRepository userRepository;

	private FollowService followService;

	private MessageSource messageSource;

	private FavService favService;

	public AdControllerTest() {
		super();
	}

	@BeforeEach
	public final void setUpMockContext() throws InstanceNotFoundException {

		Utils.setAuthentication("user");

		mockMvc = MockMvcBuilders.standaloneSetup(getController()).build();
	}

	private final AdController getController() throws InstanceNotFoundException {

		adService = Mockito.mock(AdService.class);
		favService = Mockito.mock(FavService.class);
		followService = Mockito.mock(FollowService.class);
		userRepository = Mockito.mock(UserRepository.class);
		messageSource = Mockito.mock(MessageSource.class);
		adUrlService = Mockito.mock(AdUrlService.class);

		Mockito.when(userRepository.findByUserName(Mockito.any(String.class)))
				.thenReturn(Optional.of(Utils.createUser()));

		Mockito.when(adService.findAd(Mockito.any(long.class))).thenReturn(Utils.createAdWithAdUrl());

		Mockito.when(adService.updateAdUrls(Mockito.any(Ad.class), Mockito.anyList())).thenReturn(null);

		Mockito.when(favService.showFavs(Mockito.any(String.class))).thenReturn(new ArrayList<Long>());

		Mockito.when(adService.customFindAds(Mockito.any(String.class), Mockito.any(String.class),
				Mockito.any(LocalDate.class), Mockito.any(LocalDate.class), Mockito.any(BigDecimal.class),
				Mockito.any(BigDecimal.class), Mockito.any(Float.class))).thenReturn(new ArrayList<Ad>());

		Mockito.when(adUrlService.findAdImage(Mockito.any(Ad.class), Mockito.anyLong()))
				.thenReturn(Utils.createAdUrl());

		Mockito.when(followService.findByFollowerPK_User(Mockito.any(User.class)))
				.thenReturn(new ArrayList<Follower>());

		AdController controller = new AdController(adService, messageSource, favService, userRepository, followService,
				adUrlService);

		return controller;
	}

	@Test
	public final void getAdViewRequestTest() throws Exception {

		final ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/ads/1"));

		result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@Test
	public final void adSearchCustomTest() throws Exception {

		final ResultActions result = mockMvc
				.perform(MockMvcRequestBuilders.get("/ads/search").param("keywords", "test test").param("city", "city")
						.param("date_start", "2020-02-20").param("date_end", "2020-02-20").param("price_min", "0.1")
						.param("price_max", "0.1").param("val_min", "1"));

		result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@Test
	public final void adSearchCustomNullsTest() throws Exception {

		final ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/ads/search"));

		result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@Test
	public final void adSearchCustomEmptyStringsTest() throws Exception {

		final ResultActions result = mockMvc
				.perform(MockMvcRequestBuilders.get("/ads/search").param("keywords", "").param("city", ""));

		result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@Test
	public final void doAdSearchCustomTest() throws Exception {

		final ResultActions result = mockMvc.perform(postFormRequest());

		result.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@Test
	public final void doAdSearchCustomNullTest() throws Exception {

		final ResultActions result = mockMvc.perform(postFormRequestNulls());

		result.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@Test
	public final void doAdSearchCustomEmptyTest() throws Exception {

		final ResultActions result = mockMvc.perform(postFormRequestEmpty());

		result.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@Test
	public final void doAdSearchCustomDateErrorsTest() throws Exception {

		final ResultActions result = mockMvc.perform(postFormRequestDateError());

		result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@Test
	public final void doAdSearchCustomPriceErrorTest() throws Exception {

		final ResultActions result = mockMvc.perform(postFormRequestPriceError());

		result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@Test
	public final void doAdSearchCustomValidationErrorTest() throws Exception {

		final ResultActions result = mockMvc.perform(postFormRequestValidationError());

		result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@Test
	public final void getAdPreviewImage() throws Exception {

		final ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/ads/1/images/1"));

		result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	private final RequestBuilder postFormRequest() throws IOException {

		AdSearchCustomDto ad = new AdSearchCustomDto();
		ad.setCity("city");
		ad.setKeywords("keywords");
		ad.setDate_end(LocalDate.now());
		ad.setDate_start(LocalDate.now());
		ad.setPrice_max(new BigDecimal(10));
		ad.setPrice_min(new BigDecimal(10));
		ad.setVal_min(1);

		return MockMvcRequestBuilders.multipart("/ads/search").flashAttr("search", ad);
	}

	private final RequestBuilder postFormRequestNulls() throws IOException {

		AdSearchCustomDto ad = new AdSearchCustomDto();

		return MockMvcRequestBuilders.multipart("/ads/search").flashAttr("search", ad);
	}

	private final RequestBuilder postFormRequestEmpty() throws IOException {

		AdSearchCustomDto ad = new AdSearchCustomDto();
		ad.setCity("");
		ad.setKeywords("");

		return MockMvcRequestBuilders.multipart("/ads/search").flashAttr("search", ad);
	}

	private final RequestBuilder postFormRequestPriceError() throws IOException {

		AdSearchCustomDto ad = new AdSearchCustomDto();
		ad.setCity("city");
		ad.setKeywords("keywords");
		ad.setDate_end(LocalDate.now());
		ad.setDate_start(LocalDate.now());
		ad.setPrice_max(new BigDecimal(9));
		ad.setPrice_min(new BigDecimal(11));

		return MockMvcRequestBuilders.multipart("/ads/search").flashAttr("search", ad);
	}

	private final RequestBuilder postFormRequestDateError() throws IOException {

		AdSearchCustomDto ad = new AdSearchCustomDto();
		ad.setCity("city");
		ad.setKeywords("keywords");
		ad.setDate_end(LocalDate.now().minusDays(1));
		ad.setDate_start(LocalDate.now());
		ad.setPrice_max(new BigDecimal(10));
		ad.setPrice_min(new BigDecimal(10));

		return MockMvcRequestBuilders.multipart("/ads/search").flashAttr("search", ad);
	}

	private final RequestBuilder postFormRequestValidationError() throws IOException {

		AdSearchCustomDto ad = new AdSearchCustomDto();

		ad.setPrice_max(new BigDecimal(-0.1));
		ad.setPrice_min(new BigDecimal(-0.1));

		return MockMvcRequestBuilders.multipart("/ads/search").flashAttr("search", ad);
	}

}
