package es.udc.fi.dc.fd.service;

import java.util.List;
import java.util.Optional;

import es.udc.fi.dc.fd.model.persistence.Chat;
import es.udc.fi.dc.fd.model.persistence.Message;
import es.udc.fi.dc.fd.model.persistence.User;

public interface ChatService {

	public Chat createChatRoom(User seller, User user);

	public List<Chat> findChatsByUserIdOrSellerId(long userId);

	public Message saveMessage(Message message);

	public Chat getChatByOwners(long sellerId, long userId);

	public boolean existsChat(long chatId);

	public Optional<Chat> findChat(long chatId);

	public List<Message> getChatMessages(long chatId);
}
