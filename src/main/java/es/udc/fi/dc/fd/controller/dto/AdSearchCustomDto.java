package es.udc.fi.dc.fd.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.format.annotation.DateTimeFormat;

public class AdSearchCustomDto {

	private String keywords;

	private String city;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date_start;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date_end;

	@DecimalMin(value = "0.0")
	@Digits(integer = 5, fraction = 2)
	private BigDecimal price_min;

	@DecimalMin(value = "0.0")
	@Digits(integer = 5, fraction = 2)
	private BigDecimal price_max;

	@Min(0)
	@Max(5)
	private int val_min;

	public AdSearchCustomDto() {
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public LocalDate getDate_start() {
		return date_start;
	}

	public void setDate_start(LocalDate date_start) {
		this.date_start = date_start;
	}

	public LocalDate getDate_end() {
		return date_end;
	}

	public void setDate_end(LocalDate date_end) {
		this.date_end = date_end;
	}

	public BigDecimal getPrice_min() {
		return price_min;
	}

	public void setPrice_min(BigDecimal price_min) {
		this.price_min = price_min;
	}

	public BigDecimal getPrice_max() {
		return price_max;
	}

	public void setPrice_max(BigDecimal price_max) {
		this.price_max = price_max;
	}

	public int getVal_min() {
		return val_min;
	}

	public void setVal_min(int val_min) {
		this.val_min = val_min;
	}

}
