package es.udc.fi.dc.fd.model.persistence;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "Follower")
@Table(name = "follower")
public class Follower {

	@EmbeddedId
	private FollowerPK followerPK;

	public Follower() {
	}

	public Follower(FollowerPK followerPK) {
		this.followerPK = followerPK;
	}

	public FollowerPK getFollowerPK() {
		return followerPK;
	}

	public void setFollowerPK(FollowerPK followerPK) {
		this.followerPK = followerPK;
	}

}
