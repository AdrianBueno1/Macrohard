package es.udc.fi.dc.fd.model.persistence;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class AdUrlPK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "adId")
	private Ad ad;

	private long adUrlId;

	public AdUrlPK() {
		super();
	}

	public AdUrlPK(Ad ad2, long key) {
		this.ad = ad2;
		this.adUrlId = key;
	}

	public Ad getAd() {
		return ad;
	}

	public void setAd(Ad ad) {
		this.ad = ad;
	}

	public long getAdUrlId() {
		return adUrlId;
	}

	public void setAdUrlId(long adUrlId) {
		this.adUrlId = adUrlId;
	}

}
