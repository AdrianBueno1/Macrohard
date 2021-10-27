package es.udc.fi.dc.fd.test.unit.controller.form;

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

import es.udc.fi.dc.fd.controller.entity.HoldAdController;
import es.udc.fi.dc.fd.model.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.fd.service.AdService;
import es.udc.fi.dc.fd.test.config.Utils;

@RunWith(JUnitPlatform.class)
@SpringJUnitConfig
@Transactional
@Rollback
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:context/application-context.xml" })
@TestPropertySource({ "classpath:config/persistence-access.properties" })
public class HoldAdControllerTest {
	
	private MockMvc mockMvc;
	
	private AdService adService;
	
	private HoldAdControllerTest() {
		super();
	}
	
	@BeforeEach
	public final void setUpMockContext() throws InstanceNotFoundException {

		Utils.setAuthentication("user");

		mockMvc = MockMvcBuilders.standaloneSetup(getController()).build();
	}
	
	private final HoldAdController getController() throws InstanceNotFoundException {
		adService = Mockito.mock(AdService.class);

		Mockito.when(adService.findAd(Mockito.any(long.class))).thenReturn(Utils.createAd());
		HoldAdController controller = new HoldAdController(adService);
		return controller;
	}
	
	@Test
	public final void holdAdTest() throws Exception{
		
		final ResultActions result = mockMvc.perform(postFormHoldRequest());
		
		result.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	
	private final RequestBuilder postFormHoldRequest() {

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		Long p = 0L;
		params.add("adId", p.toString());

		return MockMvcRequestBuilders.post("/hold").params(params);

	}

}
