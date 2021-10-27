package es.udc.fi.dc.fd.controller.entity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.udc.fi.dc.fd.controller.dto.MessageDto;
import es.udc.fi.dc.fd.model.persistence.Chat;
import es.udc.fi.dc.fd.model.persistence.Message;
import es.udc.fi.dc.fd.model.persistence.User;
import es.udc.fi.dc.fd.repository.UserRepository;
import es.udc.fi.dc.fd.service.ChatService;

@Controller
@RequestMapping("/chats")
public class ChatController {

	private ChatService chatService;

	private UserRepository userRepository;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	public ChatController(ChatService chatService, UserRepository userRepository,
			SimpMessagingTemplate messagingTemplate) {
		super();
		this.chatService = chatService;
		this.userRepository = userRepository;
		this.messagingTemplate = messagingTemplate;
	}

	@GetMapping
	public String showChat(ModelMap model, Authentication authentication, HttpSession session) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByUserName(auth.getName().toString()).get();
		model.addAttribute("my_id", user.getId());

		return "chat";
	}

	@PostMapping("/createChat")
	public String createChat(@RequestParam("sellerId") Long sellerId, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByUserName(auth.getName().toString()).get();
		User seller = userRepository.findById(sellerId).get();

		Chat chat = chatService.getChatByOwners(seller.getId(), user.getId());

		if (chat == null) {
			Chat chat2 = chatService.getChatByOwners(user.getId(), seller.getId());

			if (chat2 == null) {
				chat = chatService.createChatRoom(seller, user);
			} else {
				chat = chat2;
			}
		}

		redirectAttributes.addAttribute("chat_Id", chat.getChatId());

		return "redirect:/chats";
	}

	@MessageMapping("/chat/{chatId}")
	public void processMessage(@Payload MessageDto message, @DestinationVariable String chatId) {

		long id = Long.parseLong(chatId);

		if (chatService.existsChat(id)) {

			chatService.saveMessage(new Message(chatService.findChat(id).get(), message.getFromId(),
					message.getMessage(), toMillis(message.getDate())));

			messagingTemplate.convertAndSend("/topic/messages/" + chatId, message);
		}

	}

	private final LocalDateTime toMillis(long longValue) {
		return Instant.ofEpochMilli(longValue).atZone(ZoneOffset.systemDefault()).toLocalDateTime();
	}

}
