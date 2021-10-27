package es.udc.fi.dc.fd.test.unit.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.model.persistence.Chat;
import es.udc.fi.dc.fd.model.persistence.Message;
import es.udc.fi.dc.fd.model.persistence.User;
import es.udc.fi.dc.fd.model.persistence.User.RoleType;
import es.udc.fi.dc.fd.repository.ChatRepository;
import es.udc.fi.dc.fd.repository.MessageRepository;
import es.udc.fi.dc.fd.repository.UserRepository;
import es.udc.fi.dc.fd.service.ChatService;

@RunWith(JUnitPlatform.class)
@SpringJUnitConfig
@Transactional
@Rollback
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:context/application-context.xml" })
@TestPropertySource({ "classpath:config/persistence-access.properties" })
public class ChatServiceDefaultImplTest {

	@Autowired
	private ChatRepository chatRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private ChatService chatService;

	private User createUser(String username) {
		return new User(username, username, username, username, username + "@gmail.com", "City Test",
				"1234567890123456", RoleType.USER);
	}

	@Test
	public void createChatRoomTest() {

		User user = userRepository.save(createUser("user"));
		User seller = userRepository.save(createUser("seller"));

		Chat chat = chatService.createChatRoom(seller, user);

		Assert.assertEquals(chat, chatRepository.findByUserIdAndSellerId(user.getId(), seller.getId()));
	}

	@Test
	public void findChatsByUserIdOrSellerIdTest() {

		User user = userRepository.save(createUser("user"));
		User seller = userRepository.save(createUser("seller"));

		Chat chat = chatService.createChatRoom(seller, user);

		List<Chat> chats = new ArrayList<Chat>();
		chats.add(chat);

		Assert.assertEquals(chats, chatService.findChatsByUserIdOrSellerId(user.getId()));
		Assert.assertEquals(chats, chatService.findChatsByUserIdOrSellerId(seller.getId()));
	}

	@Test
	public void saveMessageTest() {

		User user = userRepository.save(createUser("user"));
		User seller = userRepository.save(createUser("seller"));

		Chat chat = chatService.createChatRoom(seller, user);

		chatService.saveMessage(new Message(chat, user.getId(), "mensaje 1", LocalDateTime.now()));
		chatService.saveMessage(new Message(chat, seller.getId(), "mensaje 2", LocalDateTime.now()));
		chatService.saveMessage(new Message(chat, user.getId(), "mensaje 3", LocalDateTime.now()));

		Assert.assertEquals(3, messageRepository.findByChatChatId(chat.getChatId()).size());
	}

	@Test
	public void getChatByOwnersTest() {

		User user = userRepository.save(createUser("user"));
		User seller = userRepository.save(createUser("seller"));

		Chat chat = chatService.createChatRoom(seller, user);

		Assert.assertEquals(chat, chatService.getChatByOwners(seller.getId(), user.getId()));
	}

	@Test
	public void existsChatTest() {

		User user = userRepository.save(createUser("user"));
		User seller = userRepository.save(createUser("seller"));

		Chat chat = chatService.createChatRoom(seller, user);

		Assert.assertTrue(chatService.existsChat(chat.getChatId()));
	}

	@Test
	public void existsChatFalseTest() {
		Assert.assertFalse(chatService.existsChat(-1));
	}

	@Test
	public void findChatTest() {

		User user = userRepository.save(createUser("user"));
		User seller = userRepository.save(createUser("seller"));

		Chat chat = chatService.createChatRoom(seller, user);

		Assert.assertEquals(chat, chatService.findChat(chat.getChatId()).get());
	}

	@Test
	public void getChatMessagesTest() {

		User user = userRepository.save(createUser("user"));
		User seller = userRepository.save(createUser("seller"));

		Chat chat = chatService.createChatRoom(seller, user);

		List<Message> messages = new ArrayList<Message>();

		messages.add(chatService.saveMessage(new Message(chat, user.getId(), "mensaje 1", LocalDateTime.now())));
		messages.add(chatService.saveMessage(new Message(chat, seller.getId(), "mensaje 2", LocalDateTime.now())));
		messages.add(chatService.saveMessage(new Message(chat, user.getId(), "mensaje 3", LocalDateTime.now())));

		Assert.assertEquals(messages, chatService.getChatMessages(chat.getChatId()));
	}

}
