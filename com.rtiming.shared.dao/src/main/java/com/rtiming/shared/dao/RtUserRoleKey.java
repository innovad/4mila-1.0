package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the rt_user_role database table.
 * 
 */
@Embeddable
public class RtUserRoleKey implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="user_nr")
	private Long userNr;

	@Column(name="client_nr")
	private Long clientNr;

	@Column(name="role_uid")
	private Long roleUid;

	public RtUserRoleKey() {
	}
	public Long getUserNr() {
		return this.userNr;
	}
	public void setUserNr(Long userNr) {
		this.userNr = userNr;
	}
	public Long getClientNr() {
		return this.clientNr;
	}
	public void setClientNr(Long clientNr) {
		this.clientNr = clientNr;
	}
	public Long getRoleUid() {
		return this.roleUid;
	}
	public void setRoleUid(Long roleUid) {
		this.roleUid = roleUid;
	}

	@Override
  public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof RtUserRoleKey)) {
			return false;
		}
		RtUserRoleKey castOther = (RtUserRoleKey)other;
		return 
			this.userNr.equals(castOther.userNr)
			&& this.clientNr.equals(castOther.clientNr)
			&& this.roleUid.equals(castOther.roleUid);
	}

	@Override
  public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.userNr.hashCode();
		hash = hash * prime + this.clientNr.hashCode();
		hash = hash * prime + this.roleUid.hashCode();
		
		return hash;
	}
}