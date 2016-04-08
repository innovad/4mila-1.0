package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the rt_startlist_setting_option database table.
 * 
 */
@Embeddable
public class RtStartlistSettingOptionKey implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="startlist_setting_nr")
	private Long startlistSettingNr;

	@Column(name="client_nr")
	private Long clientNr;

	@Column(name="option_uid")
	private Long optionUid;

	public RtStartlistSettingOptionKey() {
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
	public Long getOptionUid() {
		return this.optionUid;
	}
	public void setOptionUid(Long optionUid) {
		this.optionUid = optionUid;
	}

	@Override
  public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof RtStartlistSettingOptionKey)) {
			return false;
		}
		RtStartlistSettingOptionKey castOther = (RtStartlistSettingOptionKey)other;
		return 
			this.startlistSettingNr.equals(castOther.startlistSettingNr)
			&& this.clientNr.equals(castOther.clientNr)
			&& this.optionUid.equals(castOther.optionUid);
	}

	@Override
  public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.startlistSettingNr.hashCode();
		hash = hash * prime + this.clientNr.hashCode();
		hash = hash * prime + this.optionUid.hashCode();
		
		return hash;
	}
}