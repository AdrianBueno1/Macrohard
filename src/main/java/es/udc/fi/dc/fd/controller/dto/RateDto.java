package es.udc.fi.dc.fd.controller.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.sun.istack.NotNull;

public class RateDto {

	@Min(1)
	@Max(5)
	@NotNull
	private int value;

	public RateDto() {
	}

	public RateDto(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
