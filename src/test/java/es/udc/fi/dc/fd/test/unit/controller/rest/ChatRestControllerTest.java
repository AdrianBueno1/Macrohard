package es.udc.fi.dc.fd.test.unit.controller.rest;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
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

import es.udc.fi.dc.fd.controller.rest.ChatRestController;
import es.udc.fi.dc.fd.model.persistence.Chat;
import es.udc.fi.dc.fd.model.persistence.Message;
import es.udc.fi.dc.fd.service.ChatService;
import es.udc.fi.dc.fd.test.config.Utils;

@RunWith(JUnitPlatform.class)
@SpringJUnitConfig
@Transactional
@Rollback
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:context/application-context.xml" })
@TestPropertySource({ "classpath:config/persistence-access.properties" })
public class ChatRestControllerTest {

	private MockMvc mockMvc;

	private ChatService chatService;

	public ChatRestControllerTest() {
		super();
	}

	@BeforeEach
	public final void setUpMockContext() {

		Utils.setAuthentication("user");

		mockMvc = MockMvcBuilders.standaloneSetup(getController()).build();
	}

	private final ChatRestController getController() {

		final List<Message> entities1; // Returned entities
		final List<Chat> entities2; // Returned entities

		chatService = Mockito.mock(ChatService.class);

		entities1 = new ArrayList<>();
		entities1.add(Utils.createMessage());
		entities1.add(Utils.createMessage());
		entities1.add(Utils.createMessage());

		entities2 = new ArrayList<>();
		entities2.add(Utils.createChat());
		entities2.add(Utils.createChat());
		entities2.add(Utils.createChat());

		Mockito.when(chatService.getChatMessages(ArgumentMatchers.any(Long.class))).thenReturn(entities1);
		Mockito.when(chatService.findChatsByUserIdOrSellerId(ArgumentMatchers.any(Long.class))).thenReturn(entities2);

		return new ChatRestController(chatService);
	}

	@Test
	public final void getChatMessagesTest() throws Exception {

		final ResultActions result; // Request result

		result = mockMvc.perform(
				MockMvcRequestBuilders.get("/api/chat/1/messages").contentType(MediaType.APPLICATION_JSON_VALUE));

		// The operation was accepted
		result.andExpect(MockMvcResultMatchers.status().isOk());

		// The response model contains the expected attributes
		result.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)));
	}

	@Test
	public final void getChatsTest() throws Exception {

		final ResultActions result; // Request result

		result = mockMvc
				.perform(MockMvcRequestBuilders.get("/api/chat/1/chats").contentType(MediaType.APPLICATION_JSON_VALUE));

		// The operation was accepted
		result.andExpect(MockMvcResultMatchers.status().isOk());

		// The response model contains the expected attributes
		result.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)));
	}

}
