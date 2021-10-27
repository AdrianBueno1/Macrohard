package es.udc.fi.dc.fd.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.udc.fi.dc.fd.model.persistence.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByUserName(String userName);

	boolean existsByEmail(String email);

	Optional<User> findByUserName(String userName);

	Optional<User> findById(Long userId);
}
