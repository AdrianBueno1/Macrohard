package es.udc.fi.dc.fd.model.persistence;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "User")
@Table(name = "user")
public class User {

	public enum RoleType {
		USER, ADMIN, PREMIUM
	};

	public final static int PASSWORD_MAX_LENGTH = 18;
	public final static int PASSWORD_MIN_LENGTH = 6;
	public final static int CREDITCARD_LENGTH = 16;

	private Long id;
	private String userName;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private String city;
	private String creditCard;
	private RoleType role;
	private float rate;
	private int timesRated;

	public User() {
	}

	public User(String userName, String password, String firstName, String lastName, String email, String city,
			RoleType role) {

		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.role = role;
		this.city = city;
		this.rate = 0;
		this.timesRated = 0;
	}

	public User(String userName, String password, String firstName, String lastName, String email, String city,
			String creditCard, RoleType role) {
		super();
		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.city = city;
		this.creditCard = creditCard;
		this.role = role;
		this.rate = 0;
		this.timesRated = 0;
	}

	public User(String userName, String password, String firstName, String lastName, String email, String city,
			String creditCard, RoleType role, float rate, int timesRated) {
		super();
		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.city = city;
		this.creditCard = creditCard;
		this.role = role;
		this.rate = rate;
		this.timesRated = timesRated;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public RoleType getRole() {
		return role;
	}

	public void setRole(RoleType role) {
		this.role = role;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public int getTimesRated() {
		return timesRated;
	}

	public void setTimesRated(int timesRated) {
		this.timesRated = timesRated;
	}

}
