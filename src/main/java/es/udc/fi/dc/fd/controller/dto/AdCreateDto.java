package es.udc.fi.dc.fd.controller.dto;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;

import org.hibernate.validator.constraints.NotEmpty;

import com.sun.istack.NotNull;

public class AdCreateDto {

	@NotEmpty
	private String adName;

	@NotEmpty
	private String description;

	@NotEmpty
	private String city;

	@DecimalMin(value = "0.0")
	@Digits(integer = 5, fraction = 2)
	@NotNull
	private BigDecimal price;

	@NotNull
	private boolean premium;

	public AdCreateDto() {
		super();

	}

	public String getAdName() {
		return adName;
	}

	public void setAdName(String adName) {
		this.adName = adName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public boolean isPremium() {
		return premium;
	}

	public void setPremium(boolean premium) {
		this.premium = premium;
	}

}
