package es.udc.fi.dc.fd.controller.util;

import java.util.ArrayList;
import java.util.List;

import es.udc.fi.dc.fd.model.persistence.Follower;
import es.udc.fi.dc.fd.model.persistence.User;

public class FollowerConversor {

	public static User fromFollowerToUser(Follower follower) {

		return follower.getFollowerPK().getFollowed();
	}

	public static List<User> fromFollowerToUserList(List<Follower> followers) {

		List<User> users = new ArrayList<User>();

		followers.forEach(follower -> users.add(fromFollowerToUser(follower)));

		return users;
	}

	public static List<Long> fromFollowerToIdList(List<Follower> followers) {

		List<Long> users = new ArrayList<Long>();

		followers.forEach(follower -> users.add(follower.getFollowerPK().getFollowed().getId()));

		return users;
	}

}
