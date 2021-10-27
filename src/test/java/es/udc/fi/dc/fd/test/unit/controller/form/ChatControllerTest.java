package es.udc.fi.dc.fd.test.unit.controller.form;

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

import es.udc.fi.dc.fd.controller.entity.ChatController;
import es.udc.fi.dc.fd.model.persistence.User;
import es.udc.fi.dc.fd.repository.UserRepository;
import es.udc.fi.dc.fd.service.ChatService;
import es.udc.fi.dc.fd.test.config.Utils;

@RunWith(JUnitPlatform.class)
@SpringJUnitConfig
@Transactional
@Rollback
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:context/application-context.xml" })
@TestPropertySource({ "classpath:config/persistence-access.properties" })
public class ChatControllerTest {

	private MockMvc mockMvc;

	private ChatService chatService;

	private UserRepository userRepository;

	public ChatControllerTest() {
		super();
	}

	@BeforeEach
	public final void setUpMockContext() {

		Utils.setAuthentication("user");

		mockMvc = MockMvcBuilders.standaloneSetup(getController()).build();

	}

	private final ChatController getController() {

		chatService = Mockito.mock(ChatService.class);
		userRepository = Mockito.mock(UserRepository.class);

		Mockito.when(userRepository.findByUserName("test")).thenReturn(Optional.of(Utils.createNullIdUser()));

		Mockito.when(userRepository.findByUserName("user")).thenReturn(Optional.of(Utils.createUser()));

		Mockito.when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(Utils.createUser()));

		Mockito.when(chatService.existsChat(Mockito.eq(1))).thenReturn(true);

		Mockito.when(chatService.existsChat(Mockito.eq(-1))).thenReturn(true);

		Mockito.when(chatService.getChatByOwners(0L, 1L)).thenReturn(null);

		Mockito.when(chatService.getChatByOwners(0L, 0L)).thenReturn(null);

		Mockito.when(chatService.getChatByOwners(0L, 0L)).thenReturn(null);

		Mockito.when(chatService.getChatByOwners(1L, 1L)).thenReturn(Utils.createChat());

		Mockito.when(chatService.createChatRoom(Mockito.any(User.class), Mockito.any(User.class)))
				.thenReturn(Utils.createChat());

		ChatController controller = new ChatController(chatService, userRepository, null);

		return controller;
	}

	@Test
	public final void showChatTest() throws Exception {

		final ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/chats"));

		result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@Test
	public final void createChatTest() throws Exception {

		Utils.setAuthentication("test");

		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/chats/createChat").param("sellerId", "0"));

		result.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@Test
	public final void createExistingChatTest() throws Exception {

		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/chats/createChat").param("sellerId", "1"));

		result.andExpect(MockMvcResultMatchers.status().is3xxRedirection());

	}

}
