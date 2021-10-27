package es.udc.fi.dc.fd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import es.udc.fi.dc.fd.model.persistence.Chat;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

	@Query("SELECT c FROM Chat c WHERE c.seller.id = ?1 or c.user.id = ?1")
	public List<Chat> findByUserIdOrSellerId(long userId);

	public Chat findByUserIdAndSellerId(long userId, long sellerId);

}
