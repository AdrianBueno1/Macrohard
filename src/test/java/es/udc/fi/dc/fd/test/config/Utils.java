package es.udc.fi.dc.fd.test.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import es.udc.fi.dc.fd.controller.dto.ChatDto;
import es.udc.fi.dc.fd.controller.dto.MessageDto;
import es.udc.fi.dc.fd.controller.dto.UserDto;
import es.udc.fi.dc.fd.model.persistence.Ad;
import es.udc.fi.dc.fd.model.persistence.AdUrl;
import es.udc.fi.dc.fd.model.persistence.AdUrlPK;
import es.udc.fi.dc.fd.model.persistence.Chat;
import es.udc.fi.dc.fd.model.persistence.Message;
import es.udc.fi.dc.fd.model.persistence.User;
import es.udc.fi.dc.fd.model.persistence.User.RoleType;

public class Utils {

	public static void setAuthentication(String user) {
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);

		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);

		Mockito.when(authentication.getName()).thenReturn(user);
	}

	public static User createUser() {
		User u = new User("user", "user", "user", "user", "user", "user", "1234567890123456", RoleType.USER);
		u.setId(1L);

		return u;
	}

	public static User createNullIdUser() {
		User u = new User("test", "user", "user", "user", "user", "user", RoleType.USER);
		u.setId(0L);

		return u;
	}

	public static Ad createAd() {

		return new Ad(1L, "test", "test", null, "user", Utils.createUser(), true, new BigDecimal(10), "city",
				LocalDate.now(), false, false, null, 0L);

	}

	public static AdUrl createAdUrl() {

		return new AdUrl(new AdUrlPK(createAd(), 1L), "test", "test".getBytes());

	}

	public static Ad createAdWithAdUrl() {

		Ad ad = new Ad(1L, "test", "test", null, "user", Utils.createUser(), true, new BigDecimal(10), "city",
				LocalDate.now(), false, false, null, 0L);

		ad.setAdUrls(new HashSet<AdUrl>(Arrays.asList(new AdUrl(new AdUrlPK(createAd(), 1L), "test", "test".getBytes()),

				new AdUrl(new AdUrlPK(createAd(), 1L), "test", "test".getBytes()))));

		return ad;
	}

	public static Chat createChat() {
		Chat c = new Chat();
		c.setSeller(Utils.createUser());
		c.setUser(Utils.createUser());
		c.setChatId(1L);

		return c;
	}

	public static Message createMessage() {
		Message m = new Message();
		m.setChat(createChat());
		m.setDate(LocalDateTime.now());
		m.setFromId(1L);
		m.setMessage("test");
		m.setMessageId(1L);

		return m;
	}

	public static ChatDto createChatDTO() {
		ChatDto c = new ChatDto();
		c.setChatId(1L);
		c.setMyId(1L);
		c.setSeller(new UserDto(1L, "test"));

		return c;
	}

	public static MessageDto createMessageDTO() {
		MessageDto m = new MessageDto();
		m.setFromId(1L);
		m.setMessage("test");
		m.setDate(1L);

		return m;
	}
}
