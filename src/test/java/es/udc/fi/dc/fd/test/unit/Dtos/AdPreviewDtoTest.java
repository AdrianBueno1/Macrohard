package es.udc.fi.dc.fd.test.unit.Dtos;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.controller.dto.AdPreviewDto;

@RunWith(JUnitPlatform.class)
@SpringJUnitConfig
@Transactional
@Rollback
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:context/application-context.xml" })
@TestPropertySource({ "classpath:config/persistence-access.properties" })
public class AdPreviewDtoTest {

	@Test
	public void adCoverageDtoTest() {
		AdPreviewDto ad = new AdPreviewDto();
		ad.setId(1L);
		ad.setAdName("test");
		ad.setCity("test");
		ad.setUserName("test");
		ad.setHold(false);
		ad.setPrice(new BigDecimal(1));
		ad.setDate(LocalDate.now());
		ad.setDescription("test");
		ad.setUserId(1L);
		ad.setRate(new BigDecimal(1));

		ad.setHideAd(true);
		ad.setBuyDate(LocalDate.now());
		ad.setSold(true);
		ad.setPrice(new BigDecimal(1));
		ad.setPremium(true);
		ad.setTimesRated(1);
		ad.setImages(new ArrayList<Integer>());

		assertEquals(1L, ad.getId());
		assertNotNull(ad.getAdName());
		assertNotNull(ad.getCity());
		assertNotNull(ad.getDate());
		assertNotNull(ad.getRate());
		assertNotNull(ad.getDescription());
		assertNotNull(ad.getUserId());
		assertNotNull(ad.getUserName());

		assertNotNull(ad.isHold());
		assertNotNull(ad.isHideAd());
		assertNotNull(ad.getBuyDate());
		assertNotNull(ad.isSold());
		assertNotNull(ad.getPrice());
		assertNotNull(ad.isPremium());
		assertNotNull(ad.getTimesRated());
		assertNotNull(ad.getImages());
	}

}
