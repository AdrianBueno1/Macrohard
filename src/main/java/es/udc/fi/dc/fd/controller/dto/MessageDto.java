package es.udc.fi.dc.fd.controller.dto;

public class MessageDto {

	private long fromId;
	private String message;
	private long date;

	public MessageDto() {
	}

	public MessageDto(long fromId, String message, long date) {
		this.fromId = fromId;
		this.message = message;
		this.date = date;
	}

	public long getFromId() {
		return fromId;
	}

	public void setFromId(long fromId) {
		this.fromId = fromId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

}
