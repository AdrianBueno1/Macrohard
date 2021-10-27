package es.udc.fi.dc.fd.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.model.persistence.Follower;
import es.udc.fi.dc.fd.model.persistence.FollowerPK;
import es.udc.fi.dc.fd.model.persistence.User;
import es.udc.fi.dc.fd.repository.FollowerRepository;
import es.udc.fi.dc.fd.repository.UserRepository;

@Service
@Transactional
public class FollowServiceDefaultImpl implements FollowService {

	@Autowired
	private FollowerRepository followerRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public List<Follower> findByFollowerPK_User(User user) {

		return followerRepository.findByFollowerPK_User(user);
	}

	@Override
	public Long removeFollow(String userId, String followed) {

		return followerRepository.removeByFollowerPK_User_UserNameAndFollowerPK_Followed_UserName(userId, followed);
	}

	@Override
	public Follower follow(String username, String followed) {

		User user = userRepository.findByUserName(username).get();
		User user_followed = userRepository.findByUserName(followed).get();

		return followerRepository.save(new Follower(new FollowerPK(user, user_followed)));
	}

}
