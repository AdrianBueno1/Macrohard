package es.udc.fi.dc.fd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.udc.fi.dc.fd.model.persistence.Follower;
import es.udc.fi.dc.fd.model.persistence.FollowerPK;
import es.udc.fi.dc.fd.model.persistence.User;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, FollowerPK> {

	List<Follower> findByFollowerPK_User(User user);

	Long removeByFollowerPK_User_UserNameAndFollowerPK_Followed_UserName(String username1, String username2);

}
