package es.udc.fi.dc.fd.test.unit.controller.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import es.udc.fi.dc.fd.controller.util.ChatConversor;
import es.udc.fi.dc.fd.model.persistence.Chat;
import es.udc.fi.dc.fd.test.config.Utils;

@RunWith(JUnitPlatform.class)
public class ChatConversorTest {

	@Test
	public void fromChatsToChatsDTOTest() {

		List<Chat> chats = new ArrayList<Chat>();

		chats.add(Utils.createChat());
		chats.add(Utils.createChat());
		chats.add(Utils.createChat());
		chats.add(Utils.createChat());

		Assert.assertEquals(4, ChatConversor.fromChatsToChatsDTO(chats, 2L).size());

	}

}
