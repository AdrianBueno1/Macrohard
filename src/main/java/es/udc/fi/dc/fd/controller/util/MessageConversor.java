package es.udc.fi.dc.fd.controller.util;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import es.udc.fi.dc.fd.controller.dto.MessageDto;
import es.udc.fi.dc.fd.model.persistence.Message;

public class MessageConversor {

	public static MessageDto fromMessageToMessageDTO(Message message) {

		return new MessageDto(message.getFromId(), message.getMessage(),
				message.getDate().atZone(ZoneOffset.systemDefault()).toEpochSecond());

	}

	public static List<MessageDto> fromMessagesToMessagesDTO(List<Message> messages) {

		List<MessageDto> messageDtos = new ArrayList<MessageDto>();

		messages.forEach(message -> messageDtos.add(fromMessageToMessageDTO(message)));

		return messageDtos;
	}
}
