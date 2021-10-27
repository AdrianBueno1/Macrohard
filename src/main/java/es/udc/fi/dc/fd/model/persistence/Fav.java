package es.udc.fi.dc.fd.model.persistence;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "Fav")
@Table(name = "fav")
public class Fav {

	private Long id;

	private Long adId;

	private String userName;

	public Fav() {
	}

	public Fav(Long id, Long adId, String userName) {
		super();
		this.id = id;
		this.userName = userName;
		this.adId = adId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAdId() {
		return adId;
	}

	public void setAdId(Long adId) {
		this.adId = adId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
