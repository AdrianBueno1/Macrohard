package es.udc.fi.dc.fd.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.model.persistence.Ad;

@Repository
@Transactional
public interface AdRepository extends JpaRepository<Ad, Long>, JpaSpecificationExecutor<Ad> {

	boolean existsByAdName(String adName);

	Optional<Ad> findByAdName(String adName);

	List<Ad> findByUserName(String userName);

	List<Ad> findByUserIdIn(List<Long> ids);
	
	List<Ad> findAllByOrderByPremiumAsc();
	
	@Modifying
	@Query("update Ad a set a.hold = ?#{[0]} where a.id = ?#{[1]} ")
	void updateAd(boolean hold, Long id);
	
	@Modifying
	@Query("update Ad a set a.premium = ?#{[0]} where a.id = ?#{[1]} ")
	void updateAdPremium(boolean premium, Long id);
	
	List<Ad> findByBuyerId(Long buyerId);

}
