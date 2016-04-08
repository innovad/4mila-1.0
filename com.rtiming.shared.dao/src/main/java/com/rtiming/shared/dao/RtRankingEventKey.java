package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the rt_ranking_event database table.
 * 
 */
@Embeddable
public class RtRankingEventKey implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ranking_nr")
	private Long rankingNr;

	@Column(name="event_nr")
	private Long eventNr;

	@Column(name="client_nr")
	private Long clientNr;

	public RtRankingEventKey() {
	}
	public Long getRankingNr() {
		return this.rankingNr;
	}
	public void setRankingNr(Long rankingNr) {
		this.rankingNr = rankingNr;
	}
	public Long getEventNr() {
		return this.eventNr;
	}
	public void setEventNr(Long eventNr) {
		this.eventNr = eventNr;
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
		if (!(other instanceof RtRankingEventKey)) {
			return false;
		}
		RtRankingEventKey castOther = (RtRankingEventKey)other;
		return 
			this.rankingNr.equals(castOther.rankingNr)
			&& this.eventNr.equals(castOther.eventNr)
			&& this.clientNr.equals(castOther.clientNr);
	}

	@Override
  public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.rankingNr.hashCode();
		hash = hash * prime + this.eventNr.hashCode();
		hash = hash * prime + this.clientNr.hashCode();
		
		return hash;
	}
}