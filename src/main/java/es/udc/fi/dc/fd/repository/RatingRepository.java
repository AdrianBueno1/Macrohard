package es.udc.fi.dc.fd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.udc.fi.dc.fd.model.persistence.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

	Rating findByUserIdAndRatedUserId(long userId, long ratedUserId);

}
