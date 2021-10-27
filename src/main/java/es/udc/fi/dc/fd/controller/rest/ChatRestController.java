package es.udc.fi.dc.fd.controller.rest;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.udc.fi.dc.fd.controller.dto.ChatDto;
import es.udc.fi.dc.fd.controller.dto.MessageDto;
import es.udc.fi.dc.fd.controller.util.ChatConversor;
import es.udc.fi.dc.fd.controller.util.MessageConversor;
import es.udc.fi.dc.fd.service.ChatService;

@RestController
@RequestMapping("/api/chat")
public class ChatRestController {

	private ChatService chatService;

	@Autowired
	public ChatRestController(ChatService chatService) {
		super();
		this.chatService = chatService;
	}

	@GetMapping(value = "{chatId}/messages", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<MessageDto> getChatMessages(@PathVariable("chatId") long chatId, Principal principal) {

		return MessageConversor.fromMessagesToMessagesDTO(chatService.getChatMessages(chatId));
	}

	@GetMapping(value = "{userId}/chats", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ChatDto> getChats(@PathVariable("userId") long userId, Principal principal) {

		return ChatConversor.fromChatsToChatsDTO(chatService.findChatsByUserIdOrSellerId(userId), userId);
	}

}
