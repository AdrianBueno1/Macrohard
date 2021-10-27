package es.udc.fi.dc.fd.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class AdPreviewDto {

	private Long id;
	private String adName;
	private String description;
	private String userName;
	private boolean hold;
	private BigDecimal price;
	private String city;
	private LocalDate date;
	private Long userId;
	private boolean sold;
	private LocalDate buyDate;
	private boolean hideAd;
	private BigDecimal rate;
	private boolean premium;
	private int timesRated;
	private List<Integer> images;

	public AdPreviewDto() {
	}

	public AdPreviewDto(Long id, String adName, String description, String userName, boolean hold, BigDecimal price,
			String city, LocalDate date, Long userId, boolean sold, LocalDate buyDate, boolean hideAd, BigDecimal rate,
			int timesRated, List<Integer> images, boolean premium) {
		super();
		this.id = id;
		this.adName = adName;
		this.description = description;
		this.userName = userName;
		this.hold = hold;
		this.price = price;
		this.city = city;
		this.date = date;
		this.userId = userId;
		this.sold = sold;
		this.buyDate = buyDate;
		this.hideAd = hideAd;
		this.rate = rate;
		this.timesRated = timesRated;
		this.images = images;
		this.premium = premium;
	}

	public boolean isHideAd() {
		return hideAd;
	}

	public void setHideAd(boolean hideAd) {
		this.hideAd = hideAd;
	}

	public LocalDate getBuyDate() {
		return buyDate;
	}

	public void setBuyDate(LocalDate buyDate) {
		this.buyDate = buyDate;
	}

	public boolean isSold() {
		return sold;
	}

	public void setSold(boolean sold) {
		this.sold = sold;

		this.rate = new BigDecimal(0);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isHold() {
		return hold;
	}

	public void setHold(boolean hold) {
		this.hold = hold;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public boolean isPremium() {
		return premium;
	}

	public void setPremium(boolean premium) {
		this.premium = premium;
	}

	public int getTimesRated() {
		return timesRated;
	}

	public void setTimesRated(int timesRated) {
		this.timesRated = timesRated;
	}

	public List<Integer> getImages() {
		return images;
	}

	public void setImages(List<Integer> images) {
		this.images = images;
	}

}
