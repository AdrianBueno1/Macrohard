package es.udc.fi.dc.fd.controller.dto;

public class ChatDto {

	private Long chatId;
	private UserDto seller;
	private long myId;

	public ChatDto() {
	}

	public ChatDto(Long chatId, UserDto seller, long myId) {
		super();
		this.chatId = chatId;
		this.seller = seller;
		this.myId = myId;
	}

	public Long getChatId() {
		return chatId;
	}

	public void setChatId(Long chatId) {
		this.chatId = chatId;
	}

	public UserDto getSeller() {
		return seller;
	}

	public void setSeller(UserDto seller) {
		this.seller = seller;
	}

	public long getMyId() {
		return myId;
	}

	public void setMyId(long myId) {
		this.myId = myId;
	}

}
