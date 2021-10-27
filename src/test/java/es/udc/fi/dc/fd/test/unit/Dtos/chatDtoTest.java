package es.udc.fi.dc.fd.test.unit.Dtos;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.controller.dto.ChatDto;
import es.udc.fi.dc.fd.controller.dto.UserDto;

@RunWith(JUnitPlatform.class)
@SpringJUnitConfig
@Transactional
@Rollback
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:context/application-context.xml" })
@TestPropertySource({ "classpath:config/persistence-access.properties" })
public class chatDtoTest {

	@Test
	public void chatCoverageDtoTest() {

		ChatDto chat = new ChatDto();
		UserDto user = new UserDto(1L, "seller");
		chat.setChatId(1L);
		chat.setMyId(1L);
		chat.setSeller(user);
		assertNotNull(chat.getChatId());
		assertNotNull(chat.getMyId());
		assertNotNull(chat.getSeller());

	}
}
