package es.udc.fi.dc.fd.model.persistence;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity(name = "Ad")
@Table(name = "ad")
public class Ad {

	private Long id;
	private String adName;
	private String description;
	private Set<AdUrl> adUrls = new HashSet<>();
	private String userName;
	private User user;
	private boolean hold;
	private BigDecimal price;
	private String city;

	private LocalDate date;
	private boolean premium;
	private boolean sold;
	private LocalDate soldDate;
	private Long buyerId;

	public Ad() {
	}

	public Ad(Long id, String adName, String description, Set<AdUrl> adUrls, String userName, User user, boolean hold,
			BigDecimal price, String city, LocalDate date, boolean premium, boolean sold, LocalDate soldDate,
			Long buyerId) {

		this.id = id;
		this.adName = adName;
		this.description = description;
		this.adUrls = adUrls;
		this.userName = userName;
		this.user = user;
		this.hold = hold;
		this.price = price;
		this.city = city;
		this.sold = sold;
		this.date = date;
		this.premium = premium;
		this.sold = sold;
		this.soldDate = soldDate;
		this.buyerId = buyerId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public Long getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isSold() {
		return sold;
	}

	public void setSold(boolean sold) {
		this.sold = sold;
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

	@OneToMany(mappedBy = "id.ad", fetch = FetchType.EAGER)
	public Set<AdUrl> getAdUrls() {
		return adUrls;
	}

	public void setAdUrls(Set<AdUrl> adUrls) {
		this.adUrls = adUrls;
	}

	public Boolean getHold() {
		return hold;
	}

	public void setHold(boolean hold) {
		this.hold = hold;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "userId")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isPremium() {
		return premium;
	}

	public void setPremium(boolean premium) {
		this.premium = premium;
	}

	public LocalDate getSoldDate() {
		return soldDate;
	}

	public void setSoldDate(LocalDate soldDate) {
		this.soldDate = soldDate;

	}

}
