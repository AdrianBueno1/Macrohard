package es.udc.fi.dc.fd.service;

import java.util.List;

import es.udc.fi.dc.fd.model.persistence.Follower;
import es.udc.fi.dc.fd.model.persistence.User;

public interface FollowService {

	List<Follower> findByFollowerPK_User(User user);

	Long removeFollow(String username, String followed);

	Follower follow(String username, String followed);

}
