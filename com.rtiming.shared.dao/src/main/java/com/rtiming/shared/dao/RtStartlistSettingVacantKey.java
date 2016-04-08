package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the rt_startlist_setting_vacant database table.
 * 
 */
@Embeddable
public class RtStartlistSettingVacantKey implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="startlist_setting_nr")
	private Long startlistSettingNr;

	@Column(name="client_nr")
	private Long clientNr;

	@Column(name="start_time")
	private Long startTime;

	@Column(name="bib_no")
	private String bibNo;

	public RtStartlistSettingVacantKey() {
	}
	public Long getStartlistSettingNr() {
		return this.startlistSettingNr;
	}
	public void setStartlistSettingNr(Long startlistSettingNr) {
		this.startlistSettingNr = startlistSettingNr;
	}
	public Long getClientNr() {
		return this.clientNr;
	}
	public void setClientNr(Long clientNr) {
		this.clientNr = clientNr;
	}
	public Long getStartTime() {
		return this.startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	public String getBibNo() {
		return this.bibNo;
	}
	public void setBibNo(String bibNo) {
		this.bibNo = bibNo;
	}

	@Override
  public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof RtStartlistSettingVacantKey)) {
			return false;
		}
		RtStartlistSettingVacantKey castOther = (RtStartlistSettingVacantKey)other;
		return 
			this.startlistSettingNr.equals(castOther.startlistSettingNr)
			&& this.clientNr.equals(castOther.clientNr)
			&& this.startTime.equals(castOther.startTime)
			&& this.bibNo.equals(castOther.bibNo);
	}

	@Override
  public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.startlistSettingNr.hashCode();
		hash = hash * prime + this.clientNr.hashCode();
		hash = hash * prime + this.startTime.hashCode();
		hash = hash * prime + this.bibNo.hashCode();
		
		return hash;
	}
}