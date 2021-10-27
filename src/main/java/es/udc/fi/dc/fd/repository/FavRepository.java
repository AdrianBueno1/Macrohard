package es.udc.fi.dc.fd.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.udc.fi.dc.fd.model.persistence.Fav;

@Repository
public interface FavRepository extends JpaRepository<Fav, Long> {

	boolean existsByUserName(String userName);

	boolean existsByAdId(Long adId);

	Optional<Fav> findByAdId(Long adId);

	List<Fav> findByUserName(String userName);

	long deleteByAdIdAndUserName(@Param("adId") Long adId, @Param("userName") String userName);
}
