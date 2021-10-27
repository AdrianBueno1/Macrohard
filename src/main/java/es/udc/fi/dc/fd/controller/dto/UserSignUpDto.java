package es.udc.fi.dc.fd.controller.dto;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import es.udc.fi.dc.fd.model.persistence.User;

public class UserSignUpDto {

	@NotEmpty(message = "{NotEmpty.field}")
	private String username;

	@NotEmpty(message = "{NotEmpty.field}")
	private String firstName;

	@NotEmpty(message = "{NotEmpty.field}")
	private String lastName;

	@NotEmpty(message = "{NotEmpty.field}")
	private String city;
	
	@NotEmpty(message = "{NotEmpty.field}")
	@Length(min = User.CREDITCARD_LENGTH , max=User.CREDITCARD_LENGTH, message = "{User.creditCard.lenght}" )
	private String creditCard;


	@NotEmpty(message = "{NotEmpty.field}")
	@Length(min = User.PASSWORD_MIN_LENGTH, max = User.PASSWORD_MAX_LENGTH, message = "{User.password.lenght}")
	private String password;

	@NotEmpty(message = "{NotEmpty.field}")
	@Length(min = User.PASSWORD_MIN_LENGTH, max = User.PASSWORD_MAX_LENGTH, message = "{User.password.lenght}")
	private String confirmPassword;

	@Email(message = "{User.email.notValid}")
	@NotEmpty(message = "{NotEmpty.field}")
	private String email;

	@Email(message = "{User.email.notValid}")
	@NotEmpty(message = "{NotEmpty.field}")
	private String confirmEmail;

	public UserSignUpDto() {
		super();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getConfirmEmail() {
		return confirmEmail;
	}

	public void setConfirmEmail(String confirmEmail) {
		this.confirmEmail = confirmEmail;
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
	
	

}
