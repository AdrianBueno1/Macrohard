package es.udc.fi.dc.fd.controller.dto;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class UserLoginDto {

	@NotEmpty
	private String userName;

	@NotEmpty
	@Length(min = 5, max = 18)
	private String password;

	public UserLoginDto() {
		super();
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

}
