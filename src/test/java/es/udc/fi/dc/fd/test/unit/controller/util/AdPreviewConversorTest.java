package es.udc.fi.dc.fd.test.unit.controller.util;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import es.udc.fi.dc.fd.controller.dto.AdPreviewDto;
import es.udc.fi.dc.fd.controller.util.AdPreviewConversor;
import es.udc.fi.dc.fd.model.persistence.Ad;
import es.udc.fi.dc.fd.model.persistence.AdUrl;
import es.udc.fi.dc.fd.test.config.Utils;

@RunWith(JUnitPlatform.class)
public class AdPreviewConversorTest {

	@Test
	public void fromAdToAdPreviewHideTest() {

		Ad ad = new Ad(1L, "test", "test", new HashSet<AdUrl>(), "test", Utils.createUser(), true, new BigDecimal(1),
				"test", LocalDate.now(), false, false, LocalDate.now().minusDays(1), 1L);

		AdPreviewDto adPreview = AdPreviewConversor.fromAdToAdPreview(ad);

		assertTrue(adPreview.isHideAd());

	}

}
