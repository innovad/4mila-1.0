package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the rt_ucl database table.
 * 
 */
@Embeddable
public class RtUclKey implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="uc_uid")
	private Long ucUid;

	@Column(name="client_nr")
	private Long clientNr;

	@Column(name="language_uid")
	private Long languageUid;

	public RtUclKey() {
	}
	public Long getUcUid() {
		return this.ucUid;
	}
	public void setUcUid(Long ucUid) {
		this.ucUid = ucUid;
	}
	public Long getClientNr() {
		return this.clientNr;
	}
	public void setClientNr(Long clientNr) {
		this.clientNr = clientNr;
	}
	public Long getLanguageUid() {
		return this.languageUid;
	}
	public void setLanguageUid(Long languageUid) {
		this.languageUid = languageUid;
	}

	@Override
  public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof RtUclKey)) {
			return false;
		}
		RtUclKey castOther = (RtUclKey)other;
		return 
			this.ucUid.equals(castOther.ucUid)
			&& this.clientNr.equals(castOther.clientNr)
			&& this.languageUid.equals(castOther.languageUid);
	}

	@Override
  public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.ucUid.hashCode();
		hash = hash * prime + this.clientNr.hashCode();
		hash = hash * prime + this.languageUid.hashCode();
		
		return hash;
	}
}