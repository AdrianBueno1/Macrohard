package es.udc.fi.dc.fd.model.persistence;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "AdUrl")
@Table(name = "adUrl")
public class AdUrl implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private AdUrlPK id;

	private String fileName;

	private byte[] data;

	public AdUrl() {
	}

	public AdUrl(AdUrlPK id, String fileName, byte[] data) {
		super();
		this.id = id;
		this.fileName = fileName;
		this.data = data;
	}

	public AdUrlPK getId() {
		return id;
	}

	public void setId(AdUrlPK id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

}
