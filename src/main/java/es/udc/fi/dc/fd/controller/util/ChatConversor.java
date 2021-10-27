package es.udc.fi.dc.fd.controller.util;

import java.util.ArrayList;
import java.util.List;

import es.udc.fi.dc.fd.controller.dto.ChatDto;
import es.udc.fi.dc.fd.controller.dto.UserDto;
import es.udc.fi.dc.fd.model.persistence.Chat;

public class ChatConversor {

	public static ChatDto fromChatToChatDTO(Chat chat, long userId) {

		UserDto user = null;

		if (chat.getUser().getId() == userId) {

			user = new UserDto(chat.getSeller().getId(), chat.getSeller().getUserName());
		} else {

			user = new UserDto(chat.getUser().getId(), chat.getUser().getUserName());
		}

		return new ChatDto(chat.getChatId(), user, userId);

	}

	public static List<ChatDto> fromChatsToChatsDTO(List<Chat> chats, long userId) {

		List<ChatDto> chatDtos = new ArrayList<ChatDto>();

		chats.forEach(chat -> chatDtos.add(fromChatToChatDTO(chat, userId)));

		return chatDtos;
	}

}
