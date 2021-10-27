package es.udc.fi.dc.fd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.udc.fi.dc.fd.model.persistence.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

	List<Message> findByChatChatId(long chatId);
}
