package es.udc.fi.dc.fd.test.unit.controller.rest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

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

import es.udc.fi.dc.fd.controller.rest.AdSearchRestService;
import es.udc.fi.dc.fd.model.persistence.Ad;
import es.udc.fi.dc.fd.service.AdService;

@RunWith(JUnitPlatform.class)
@SpringJUnitConfig
@Transactional
@Rollback
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:context/application-context.xml" })
@TestPropertySource({ "classpath:config/persistence-access.properties" })
public class AdSearchRestControllerTest {

	private MockMvc mockMvc;

	private AdService adService;

	public AdSearchRestControllerTest() {
		super();
	}

	@BeforeEach
	public final void setUpMockContext() {

		mockMvc = MockMvcBuilders.standaloneSetup(getController()).build();
	}

	private final AdSearchRestService getController() {

		adService = Mockito.mock(AdService.class);

		Mockito.when(adService.customFindAds(Mockito.any(String.class), Mockito.any(String.class),
				Mockito.any(LocalDate.class), Mockito.any(LocalDate.class), Mockito.any(BigDecimal.class),
				Mockito.any(BigDecimal.class), Mockito.any(Float.class))).thenReturn(new ArrayList<Ad>());

		return new AdSearchRestService(adService);
	}

	@Test
	public final void adSearchCustomTest() throws Exception {

		final ResultActions result = mockMvc
				.perform(MockMvcRequestBuilders.get("/api/search").param("keywords", "test test").param("city", "city")
						.param("date_start", "2020-02-20").param("date_end", "2020-02-20").param("price_min", "0.1")
						.param("price_max", "0.1").param("val_min", "1"));

		result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@Test
	public final void adSearchCustomNullsTest() throws Exception {

		final ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api/search"));

		result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@Test
	public final void adSearchCustomEmptyStringsTest() throws Exception {

		final ResultActions result = mockMvc
				.perform(MockMvcRequestBuilders.get("/api/search").param("keywords", "").param("city", ""));

		result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

}
