package es.udc.fi.dc.fd.model.persistence;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class FollowerPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8682029928646187797L;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User user;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "followedId")
	private User followed;

	public FollowerPK() {
	}

	public FollowerPK(User user, User followed) {
		super();
		this.user = user;
		this.followed = followed;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getFollowed() {
		return followed;
	}

	public void setFollowed(User followed) {
		this.followed = followed;
	}

}
