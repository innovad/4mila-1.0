package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the rt_event_class database table.
 * 
 */
@Embeddable
public class RtEventMapKey implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="event_nr")
	private Long eventNr;

	@Column(name="map_nr")
	private Long mapNr;

	@Column(name="client_nr")
	private Long clientNr;

	public RtEventMapKey() {
	}
	public Long getEventNr() {
		return this.eventNr;
	}
	public void setEventNr(Long eventNr) {
		this.eventNr = eventNr;
	}
	
	public Long getMapNr() {
		return mapNr;
	}
	
	public void setMapNr(Long mapNr) {
		this.mapNr = mapNr;
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
		if (!(other instanceof RtEventMapKey)) {
			return false;
		}
		RtEventMapKey castOther = (RtEventMapKey)other;
		return 
			this.eventNr.equals(castOther.eventNr)
			&& this.mapNr.equals(castOther.mapNr)
			&& this.clientNr.equals(castOther.clientNr);
	}

	@Override
  public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.eventNr.hashCode();
		hash = hash * prime + this.mapNr.hashCode();
		hash = hash * prime + this.clientNr.hashCode();
		
		return hash;
	}
}