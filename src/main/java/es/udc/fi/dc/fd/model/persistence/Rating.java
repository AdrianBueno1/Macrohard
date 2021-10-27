package es.udc.fi.dc.fd.model.persistence;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "Rating")
@Table(name = "rating")
public class Rating {

	private Long id;
	private Long userId;
	private Long ratedUserId;
	private int value;

	public Rating() {
		super();
	}

	public Rating(Long id, Long userId, Long ratedUserId, int value) {
		super();
		this.id = id;
		this.userId = userId;
		this.ratedUserId = ratedUserId;
		this.value = value;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getRatedUserId() {
		return ratedUserId;
	}

	public void setRatedUserId(Long ratedUserId) {
		this.ratedUserId = ratedUserId;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
