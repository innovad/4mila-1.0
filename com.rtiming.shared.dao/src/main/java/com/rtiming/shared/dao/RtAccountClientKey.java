package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the rt_account_client database table.
 * 
 */
@Embeddable
public class RtAccountClientKey implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="account_nr")
	private Long accountNr;

	@Column(name="client_nr")
	private Long clientNr;

	public RtAccountClientKey() {
	}
	public Long getAccountNr() {
		return this.accountNr;
	}
	public void setAccountNr(Long accountNr) {
		this.accountNr = accountNr;
	}
	public Long getClientNr() {
		return this.clientNr;
	}
	public void setClientNr(Long clientNr) {
		this.clientNr = clientNr;
	}

	@Override
  public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof RtAccountClientKey)) {
			return false;
		}
		RtAccountClientKey castOther = (RtAccountClientKey)other;
		return 
			this.accountNr.equals(castOther.accountNr)
			&& this.clientNr.equals(castOther.clientNr);
	}

	@Override
  public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.accountNr.hashCode();
		hash = hash * prime + this.clientNr.hashCode();
		
		return hash;
	}
}