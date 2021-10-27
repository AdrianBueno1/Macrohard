package es.udc.fi.dc.fd.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.model.persistence.Chat;
import es.udc.fi.dc.fd.model.persistence.Message;
import es.udc.fi.dc.fd.model.persistence.User;
import es.udc.fi.dc.fd.repository.ChatRepository;
import es.udc.fi.dc.fd.repository.MessageRepository;

@Service
@Transactional
public class ChatServiceDefaultImpl implements ChatService {

	@Autowired
	private ChatRepository chatRepository;

	@Autowired
	private MessageRepository messageRepository;

	@Override
	public Chat createChatRoom(User seller, User user) {

		return chatRepository.save(new Chat(0L, seller, user));
	}

	@Override
	public List<Chat> findChatsByUserIdOrSellerId(long userId) {

		return chatRepository.findByUserIdOrSellerId(userId);
	}

	@Override
	public Message saveMessage(Message message) {

		return messageRepository.save(message);
	}

	@Override
	public Chat getChatByOwners(long sellerId, long userId) {

		return chatRepository.findByUserIdAndSellerId(userId, sellerId);
	}

	@Override
	public boolean existsChat(long chatId) {

		return chatRepository.existsById(chatId);
	}

	@Override
	public Optional<Chat> findChat(long chatId) {

		return chatRepository.findById(chatId);
	}

	@Override
	public List<Message> getChatMessages(long chatId) {
		return messageRepository.findByChatChatId(chatId);
	}

}
